package io.github.oowhitecatoo.superoctocompiler

import sun.nio.cs.ext.Big5

fun String.copyToClip() {
    ProcessBuilder("clip").redirectInput(ProcessBuilder.Redirect.PIPE)
        .start()
        .outputStream.apply {
            write(toByteArray(Big5()))
            flush()
        }
}