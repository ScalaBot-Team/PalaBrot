package palabrot

import canoe.api._
import canoe.syntax.{command, _}
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import palabrot.services.Storage.DBService
import palabrot.services.{MessageCleaner, Storage, Summarizer}
import palabrot.utils.Loggers.Caught
import palabrot.utils._

import scala.util.{Failure, Success, Try}

object MainFlow extends IOApp {

  val token: String = "1688218769:AAEeohAHBHx3AkKfkLoZyWfqoU4RGMGzFMw"

  def run(args: List[String]): IO[ExitCode] =
    Stream
      .resource(TelegramClient.global[IO](token))
      .flatMap { implicit client =>
        Bot.polling[IO].follow(
          messageHistory(Storage.elastic),
          summarize,
          delete(MessageCleaner.elastic),
          help
        )
      }
      .compile.drain.as(ExitCode.Success)

  def messageHistory[F[_]: TelegramClient](store: DBService[F]): Scenario[F, Unit] =
    for {
      msg <- Scenario.expect(textMessage)
      _   <- {
          Loggers.main.info(Caught, msg)
          Scenario.eval(store.addMessage(msg))
        }
    } yield ()

  def summarize[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("summarize"))
      _       <- Try(command.text.dropWhile(!_.equals(' ')).trim.toInt) match {
        case Success(value) => Scenario.eval(command.chat.send(Summarizer.elastic.summary(command, value).unsafeRunSync()))
        case Failure(_) => Scenario.eval(command.chat.send("Argumento no válido, usa /summarize número"))
      }
    } yield ()

  def delete[F[_]: TelegramClient](cleaner: MessageCleaner.DBService[F]): Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("delete"))
      _       <- Scenario.eval(cleaner.deleteMessages(command))
    } yield ()

  def help[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("help"))
      _       <- Scenario.eval(command.chat.send(Helper.display))
    } yield ()
}