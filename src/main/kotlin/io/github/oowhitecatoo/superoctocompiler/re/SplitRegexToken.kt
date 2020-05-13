package io.github.oowhitecatoo.superoctocompiler.re

import io.github.oowhitecatoo.superoctocompiler.fa.Node

sealed class SplitRegexToken(val priority: Int)

sealed class Symbol(val key: String?, val operatorCount: Int, priority: Int) : SplitRegexToken(priority) {

    override fun toString(): String = key.orEmpty()

    object LeftBracket : Symbol("(", 0, 0)
    object RightBracket : Symbol(")", 0, 0)
    object Star : Symbol("*", 1, 3)
    object Plus : Symbol("+", 1, 3)
    object Concat : Symbol(null, 2, 2) {
        override fun toString(): String = "&"
    }

    object Or : Symbol("|", 2, 1)

    companion object {

        private var symbolMap: Map<String, Symbol> = listOf(
            LeftBracket, RightBracket, Star, Plus, Or
        ).filter { it.key != null }
            .map { it.key!! to it }.toMap()

        fun check(char: Char): Symbol? = symbolMap[char.toString()]
    }
}


data class Text(val value: Char) : SplitRegexToken(0) {
    override fun toString(): String = value.toString()
}
