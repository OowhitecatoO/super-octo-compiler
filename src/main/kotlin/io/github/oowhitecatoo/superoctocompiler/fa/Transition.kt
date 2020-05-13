package io.github.oowhitecatoo.superoctocompiler.fa

data class Transition(
    val key: TransitionToken,
    val to: Node
)

sealed class TransitionToken
object NullString : TransitionToken() {
    override fun toString(): String = "λ"
}

data class TextTrans(val chars: Set<Char>) : TransitionToken() {
    constructor(vararg chars: Char) : this(chars.toSet())

    override fun toString(): String = chars.joinToString(", ")
}

