package palabrot

import canoe.api._
import canoe.syntax.{command, _}
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import palabrot.persistence.Storage.StorageService
import palabrot.persistence.{Storage, Summarizer}
import palabrot.resources._

object MainFlow extends IOApp {

  val token: String = "1688218769:AAH8Mw-WvFwhMy9a2c9XjA_bEpMGfRmjFFs"

  def run(args: List[String]): IO[ExitCode] =
    Stream
      .resource(TelegramClient.global[IO](token))
      .flatMap { implicit client =>
        Bot.polling[IO].follow(
          messageHistory(Storage.elastic),
          summarize,
          delete(Storage.elastic),
          help
        )
      }
      .compile.drain.as(ExitCode.Success)

  def messageHistory[F[_]: TelegramClient](store: StorageService[F]): Scenario[F, Unit] =
    for {
      msg <- Scenario.expect(textMessage)
      _ <- {
        Loggers.main.info("Message caught", msg)
        Scenario.eval(store.addMessage(msg))
      }
    } yield ()

  def summarize[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("summarize"))
      _    <- Scenario.eval(command.chat.send(Summarizer.summary(command)))
    } yield ()

  def delete[F[_]: TelegramClient](cleaner: Storage.StorageService[F]): Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("delete"))
      _    <- Scenario.eval(cleaner.deleteMessages(command))
    } yield ()

  def help[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("delete"))
      _    <- Scenario.eval(command.chat.send(Helper.display))
    } yield ()

}