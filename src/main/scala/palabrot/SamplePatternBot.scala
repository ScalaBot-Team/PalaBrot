package palabrot

import canoe.api._
import canoe.models._
import canoe.syntax._
import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.scalalogging.Logger
import fs2.Stream


/**
 * Example using compositional property of scenarios
 * by combining them into more complex registration process
 */
object PalabrotBot extends IOApp {

  val token: String = "token"
  val logger = Logger("main logs")

  def run(args: List[String]): IO[ExitCode] =
    Stream
      .resource(TelegramClient.global[IO](token))
      .flatMap { implicit client =>
        Bot.polling[IO].follow(messageHistory(storageService.store), summarize)
      }
      .compile.drain.as(ExitCode.Success)

  /** messageHistory is saving messages history.(It will keeping m last messages, still to be decided) */
  def messageHistory[F[_]: TelegramClient](store: storageService.Service[F]): Scenario[F, Unit] =
    for {
      msg <- Scenario.expect(textMessage)
      _ <- {
        logger.info(s"Message caught from chat ${
          (msg.chat match {
            case PrivateChat(_, username, _, _) => username
            case Group(_, title) => title
            case Supergroup(_, title, _) => title
            case Channel(_, title, _) => title
          }).getOrElse("unknown")
        } written by ${msg.from.get.firstName}")
        Scenario.eval(store.addMessage(msg))
      }
    } yield ()

  /** summarize is expecting for command "summarize" and then return a summary */
  def summarize[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      chat <- Scenario.expect(command("summarize").chat)
      _    <- Scenario.eval(chat.send(content = process(chat, 200))) //De momento he puesto 200 por defecto
    } yield ()

  /** process function takes ´n´ last messages from a ´chat´ and processes a summary
   * -> Ahora lo único que hace es recoger los mensajes y concatenarlos con un salto de línea */
  def process(chat: Chat, n: Int): String =
    storageService.store.getMessages(chat, n).unsafeRunSync().foldLeft("Summary: ")((z, s) => s"$z\n$s")



}