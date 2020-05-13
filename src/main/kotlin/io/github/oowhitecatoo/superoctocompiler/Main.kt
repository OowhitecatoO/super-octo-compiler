package io.github.oowhitecatoo.superoctocompiler

import io.github.oowhitecatoo.superoctocompiler.re.nfa2dfa

fun main(args: Array<String>) {

    if (args.isEmpty()) return println("show help")
    when (args[0]) {
        "nfa2dfa" -> {
            nfa2dfa("(a((cd)*|ef)*d*)*")
//            nfa2dfa(args[1])
        }
    }


}
