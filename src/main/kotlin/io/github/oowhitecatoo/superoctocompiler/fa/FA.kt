package io.github.oowhitecatoo.superoctocompiler.fa

import io.github.oowhitecatoo.superoctocompiler.graphviz.Graphvizable

class FA : Graphvizable {
    constructor()
    constructor(node: Node) {
        startNode = node
    }

    lateinit var startNode: Node
    lateinit var nowStatus: Node
    var isEnd = false

    override fun toGraphviz(title: String, forceRenameNode: Boolean): String {
        // rename node with number

        var n = 1

        val allNode = linkedSetOf<Node>();

        val toRename = linkedSetOf(startNode)

        while (toRename.isNotEmpty()) {
            val next = toRename.reversed().minBy { it.level }!!
            allNode += next
            toRename -= next

            if (next.name.isEmpty() || forceRenameNode)
                next.name = "${n++}"
            toRename += next.transitionList.map(Transition::to)
                .filter { node -> node !in allNode }
        }

        fun TransitionToken.escape() = toString().replace("""\""", """\\""")
            .replace('"'.toString(), """\"""")

        val trs = allNode.flatMap { node -> node.transitionList.map { node to it } }
            .sortedBy { it.second.to.name.toIntOrNull() }
            .sortedBy { it.first.name.toIntOrNull() }
            .joinToString("\n") { (a, tr) ->
                """    "${a.name}" -> "${tr.to.name}" [label="${tr.key.escape()}"]"""
            }

        val finalNodes = allNode.filter { it.isFinal }.joinToString(", ") { '"' + it.name + '"' }

        val nodeLevel = allNode.reversed().joinToString("\n") {
            """    "${it.name}" [label=<<FONT point-size="25">\N</FONT><BR/><FONT color="blue">${it.level}</FONT>>]"""
        }

        return """```graphviz
digraph {
    rankdir=LR;
    node [shape=circle]
    
    label="$title"    
    labelloc="t"
    fontsize=30

    s [color=white, label=""]
    s -> "${startNode.name}"
$trs

$nodeLevel
    $finalNodes [shape=doublecircle ]
}
```
"""
    }
}
