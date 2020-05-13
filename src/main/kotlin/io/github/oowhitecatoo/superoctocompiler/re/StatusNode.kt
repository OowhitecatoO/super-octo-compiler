package io.github.oowhitecatoo.superoctocompiler.re

class StatusNode(
    var name: String,
    val isFinal: Boolean
) {

    fun merge(node: StatusNode, newName: String) =
        StatusNode(newName, node.isFinal || this.isFinal)

    operator fun plus(node: StatusNode) {

    }

}