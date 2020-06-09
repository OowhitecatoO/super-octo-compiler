package io.github.oowhitecatoo.superoctocompiler.fa


fun nfa2dfa(fa: FA): FA {
    val nodeSet = mutableSetOf<Node>()

    val mergeNodeMap = mutableMapOf<NodeSet, Node>()

    // Pair<isNew, Node>
    fun NodeSet.makeNode() =
        !mergeNodeMap.contains(this) to mergeNodeMap.computeIfAbsent(this) {
            Node(0).apply {
                name = this@makeNode.set.joinToString { it.name }
                isFinal = this@makeNode.set.any { it.isFinal }
            }
        }

    val start = fa.startNode.lambdaClosure().makeNode().second

    val toFind = mergeNodeMap.keys.toMutableSet()

    while (toFind.isNotEmpty()) {
        val nextFind = mutableSetOf<NodeSet>()

        toFind.forEach { thisSet ->
            val thisNode = mergeNodeMap[thisSet]!!
            thisSet.keysMap.forEach { (char, nodeList) ->
                val newSet = nodeList.map { it.lambdaClosure() }.reduce(NodeSet::plus)
                val (isNew, node) = newSet.makeNode()
                thisNode with TextTrans(char) goto node
                if (isNew) nextFind += newSet
            }
        }

        toFind.clear()
        toFind += nextFind
    }

    return FA(start)
}

//fun dfaOptimization(dfa: FA): FA {
//
//}

private data class NodeSet(
    val set: LinkedHashSet<Node>
) {
    constructor(set: Set<Node>) : this(LinkedHashSet(set))

    operator fun plus(nodeSet: NodeSet) = NodeSet(this.set + nodeSet.set)

    val keysMap by lazy {
        set.flatMap { it.transitionList }
            .map { it.key to it.to }
            .filter { it.first is TextTrans }
            .filterIsInstance<Pair<TextTrans, Node>>()
            .groupBy({ it.first.char }) { it.second }
    }
}

private fun Node.lambdaClosure(): NodeSet {
    val open = linkedSetOf(this)
    val close = hashSetOf<Node>()
    val find = linkedSetOf<Node>()

    while (open.isNotEmpty()) {
        find += open
        close += open

        val nextOpen = mutableSetOf<Node>()
        open.flatMap { it.transitionList }
            .filter { it.key is NullString }
            .filter { it.to !in close }
            .mapTo(nextOpen) { it.to }

        open.clear()
        open += nextOpen
    }

    return NodeSet(find)
}