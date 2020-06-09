package io.github.oowhitecatoo.superoctocompiler.fa

class NFAMergeNode(val head: Node, val tail: Node)

infix fun Node.with(token: TransitionToken) = this.toNFAMergeNode() to token
infix fun NFAMergeNode.with(token: TransitionToken) = this to token


private infix fun TransitionToken.goto(other: Node) =
    Transition(this, other)

infix fun Pair<NFAMergeNode, TransitionToken>.goto(other: NFAMergeNode) =
    NFAMergeNode(first.apply { tail += second goto other.head }.head, other.tail)

infix fun Pair<NFAMergeNode, TransitionToken>.goto(other: Node) =
    this goto other.toNFAMergeNode()

class Node(
    val level: Int,
    val transitionList: MutableList<Transition> = mutableListOf(),
    var name: String = "",
    var isFinal: Boolean = false
) {
    operator fun plusAssign(transition: Transition) {
        transitionList += transition
    }

    fun toNFAMergeNode(other: Node = this) = NFAMergeNode(this, other)
}
