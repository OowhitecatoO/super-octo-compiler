package io.github.oowhitecatoo.superoctocompiler

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import io.github.oowhitecatoo.superoctocompiler.re.regex2dfa
import java.io.File

class MainCommand(parser: ArgParser) {
    val source by parser.adding("-f", "--file", help = "source file", transform = ::File)
        .default(emptyList<File>())
        .addValidator { value.find { !it.exists() }?.run { throw InvalidArgumentException("${this.name} not exists") } }

    val regex2dfa by parser.storing("-r", "--regexTodfa", help = "").default<String?>(null)
}

fun main(args: Array<String>): Unit = mainBody {

    val config = ArgParser(args.ifEmpty { arrayOf("--help") }).parseInto(::MainCommand)

    config.regex2dfa?.also { regexString ->
        regex2dfa(regexString)
    }

    if (config.source.isNotEmpty()) {
        val text = config.source.map { it.readText() }

    }

}
