package io.github.oowhitecatoo.superoctocompiler.graphviz

interface Graphvizable {
    fun toGraphviz(title: String = "", forceRenameNode: Boolean = false): String
}