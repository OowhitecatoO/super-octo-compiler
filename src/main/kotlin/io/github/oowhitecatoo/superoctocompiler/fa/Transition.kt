package io.github.oowhitecatoo.superoctocompiler.fa

data class Transition(
    val key: TransitionToken,
    val to: Node
)

sealed class TransitionToken
object NullString : TransitionToken() {
    override fun toString(): String = "λ"
}

data class TextTrans(val char: Char) : TransitionToken() {
//    constructor(char: Char) : this(chars.toSet())

    override fun toString(): String = char.toString()
}

