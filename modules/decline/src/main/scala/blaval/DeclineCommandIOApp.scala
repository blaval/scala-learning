package blaval

import blaval.DockerCLI.buildOpts
import blaval.DockerCLI.showProcessesOpts
import blaval.DockerLogic.interpret
import blaval.DockerModels.BuildImage
import blaval.DockerModels.DockerModels
import blaval.DockerModels.ShowProcesses
import cats.effect.ExitCode
import cats.effect.IO
import cats.implicits.catsSyntaxTuple2Semigroupal
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp

object DockerModels {
  sealed trait DockerModels
  case class ShowProcesses(all: Boolean)                          extends DockerModels
  case class BuildImage(dockerFile: Option[String], path: String) extends DockerModels
}

object DockerCLI {

  val showProcessesOpts: Opts[ShowProcesses] =
    Opts.subcommand("ps", "Lists docker processes running!") {
      Opts
        .flag("all", "Whether to show all running processes.", short = "a")
        .orFalse
        .map(ShowProcesses)
    }

  val dockerFileOpts: Opts[Option[String]] =
    Opts.option[String]("file", "The name of the Dockerfile.", short = "f").orNone

  val pathOpts: Opts[String] = Opts.argument[String](metavar = "path")

  val buildOpts: Opts[BuildImage] =
    Opts.subcommand("build", "Builds a docker image!") {
      (dockerFileOpts, pathOpts).mapN(BuildImage)
    }

}

object DockerLogic {

  def interpret[A <: DockerModels](opts: Opts[A]): Opts[IO[ExitCode]] = {
    println(opts)
    opts map {
      case ShowProcesses(all)           => println(all); IO(ExitCode.Success)
      case BuildImage(dockerFile, path) => println(path); println(dockerFile); IO(ExitCode.Success)
      case e                            => println(e); IO(ExitCode.Success)
    }
  }

}

object DeclineCommandIOApp
    extends CommandIOApp(name = "docker", header = "Fake docker command line", version = "0.0.x") {

  override def main: Opts[IO[ExitCode]] = {
    println("OKKDSOKDOSK")
    interpret(showProcessesOpts orElse buildOpts)
  }

}
