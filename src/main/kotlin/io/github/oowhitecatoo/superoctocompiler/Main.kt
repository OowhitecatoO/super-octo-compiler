package io.github.oowhitecatoo.superoctocompiler

import io.github.oowhitecatoo.superoctocompiler.re.regex2dfa

fun main(args: Array<String>) {

    if (args.isEmpty()) return println("show help")
    when (args[0]) {
        "nfa2dfa" -> {
            regex2dfa("ab*")
//            nfa2dfa(args[1])
        }
    }


}
