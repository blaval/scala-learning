package blaval

import cats.implicits._
import com.monovore.decline.CommandApp
import com.monovore.decline.Opts

import java.nio.file.Path

object DeclineCommandApp
    extends CommandApp(
      name = "hello-world",
      header = "Says hello!",
      main = {
        val userOpt =
          Opts.option[String]("target", help = "Person to greet.").withDefault("world")
        val lineOpt: Opts[Int] =
          Opts.option[Int]("lines", short = "n", metavar = "count", help = "Set a number of lines.")
        val settings                = Opts.options[String]("setting", help = "...")
        val quietOpt: Opts[Boolean] = Opts.flag("quiet", help = "Don't print any metadata to the console.").orFalse
        val verbose                 = Opts.flags("verbose", help = "Print extra metadata to the console.")
        val file: Opts[Path]        = Opts.argument[Path](metavar = "file")
        val files                   = Opts.arguments[String]("file")
        val port                    = Opts.env[Int]("PORT", help = "The port to run on.")

        (userOpt, quietOpt).mapN { (user, quiet) =>
          if (quiet) println("...")
          else println(s"Hello $user!")
        }

        (lineOpt, settings, verbose, file, files, port).mapN { (line, settings, verbose, file, files, port) =>
          println(line)
          println(settings)
          println(verbose)
          println(file)
          println(files)
          println(port)
        }
      }
    )
