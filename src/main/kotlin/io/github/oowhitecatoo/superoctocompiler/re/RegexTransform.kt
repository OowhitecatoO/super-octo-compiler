package io.github.oowhitecatoo.superoctocompiler.re

import io.github.oowhitecatoo.superoctocompiler.copyToClip
import io.github.oowhitecatoo.superoctocompiler.fa.*
import java.util.*

fun regex2dfa(re: String) {

    buildString {
        val nfa = str2NFA(re)
        append(nfa.toGraphviz(re))
        val dfa = nfa2dfa(nfa)
        append(dfa.toGraphviz(re, true))

    }.copyToClip()
}

fun str2NFA(re: String): FA {
    val rt = RegexTransform(re)
    val postfix = rt.postfix


    println(postfix)
    println(rt.re)

    val nfa = postfix2NFA(postfix)

    println()
//    val output = nfa.toGraphviz()
//        .also { toCopy += it }
////        .also { it.copyToClip() } // Windows OS only, TODO linux copyToClip
//    println(output)

    return nfa

}

class RegexTransform(val re: String) {
    private val stack = Stack<Symbol>()
    private val output = Stack<SplitRegexToken>()
    private var lastIsText = false
    private val bracketConcatStack = Stack<Boolean>()


    private fun pushText(char: Char) {
        if (lastIsText)
            pushSymbol(Symbol.Concat)
        lastIsText = true
        output.push(Text(char))
    }

    private fun pushSymbol(symbol: Symbol) {
        if (symbol !is Symbol.Concat && symbol.operatorCount > 1)
            lastIsText = false

        when (symbol) {
            is Symbol.LeftBracket -> {
                stack.push(symbol)

                bracketConcatStack.push(lastIsText)
                lastIsText = false
            }

            Symbol.RightBracket -> {
                while (stack.peek() !is Symbol.LeftBracket)
                    output.push(stack.pop())
                stack.pop()

                lastIsText = bracketConcatStack.pop()
                if (lastIsText) pushSymbol(Symbol.Concat)
            }

            else -> {
                if (symbol.operatorCount < 2)
                    output.push(symbol)
                else {
                    while (stack.isNotEmpty() && stack.peek().priority >= symbol.priority)
                        output.push(stack.pop())
                    stack.push(symbol)
                }
            }
        }
    }

    val postfix: List<SplitRegexToken> by lazy { calc() }

    private fun calc(): List<SplitRegexToken> {
        var escape = false

        for (c in re) {
            println(c)

            if (c == '\\' && !escape) {
                escape = true
                continue
            }
            if (escape) {
                escape = false
                pushText(c)
                continue
            }

            val symbol = Symbol.check(c)
            if (symbol != null)
                pushSymbol(symbol)
            else
                pushText(c)
        }

        while (stack.isNotEmpty()) {
            println(stack)
            output.push(stack.pop())
        }

        return output.toList()
    }
}

fun postfix2NFA(postfix: List<SplitRegexToken>): FA {
    val nodeStack = Stack<NFAMergeNode>()
    var gLevel = 0
    for (token in postfix) {
        when (token) {
            Symbol.LeftBracket, Symbol.RightBracket -> Unit
            Symbol.Star -> {
                val a = nodeStack.pop()!!
                val start = Node(++gLevel)
                val end = Node(gLevel)

                start with NullString goto end
                start with NullString goto a with NullString goto end

                a.apply { tail with NullString goto head }

                nodeStack.push(start.toNFAMergeNode(end))
            }
            Symbol.Plus -> TODO()
            Symbol.Concat -> {
                val b = nodeStack.pop()!!
                val a = nodeStack.pop()!!
                nodeStack.push(a with NullString goto b)
            }
            Symbol.Or -> {
                val b = nodeStack.pop()!!
                val a = nodeStack.pop()!!
                val end = Node(++gLevel)
                val start = Node(gLevel)

                start with NullString goto a with NullString goto end
                start with NullString goto b with NullString goto end

                nodeStack.push(start.toNFAMergeNode(end))
            }
            is Text -> {
                nodeStack += Node(gLevel) with TextTrans(token.value) goto Node(gLevel)
            }
        }
    }

    println("> " + nodeStack.size)
    val final: NFAMergeNode = nodeStack.toList().reduce { a, b -> a with NullString goto b }
    final.tail.isFinal = true

    return FA(final.head)
}
