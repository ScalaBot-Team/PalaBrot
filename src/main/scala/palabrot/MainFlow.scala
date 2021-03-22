package palabrot

import canoe.api._
import canoe.syntax.{command, _}
import cats.effect.{ExitCode, IO, IOApp}
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import fs2.Stream
import palabrot.services.{ElasticRepository, MessageRepository, Summarizer}
import palabrot.utils.Loggers.Caught
import palabrot.utils._

import scala.util.{Failure, Success, Try}

object MainFlow extends IOApp {

  val token: String = System.getenv("BOT_TOKEN")
  val client = ElasticClient(
    JavaClient(
      ElasticProperties(s"http://${sys.env.getOrElse("ES_HOST", "127.0.0.1")}:${sys.env.getOrElse("ES_PORT", "9200")}")
    )
  )
  val DB: MessageRepository = new ElasticRepository(client, "Messages")

  def run(args: List[String]): IO[ExitCode] =
    Stream
      .resource(TelegramClient.global[IO](token))
      .flatMap { implicit client =>
        Bot.polling[IO].follow(
          messageHistory(DB),
          summarize,
          delete(DB),
          help
        )
      }
      .compile.drain.as(ExitCode.Success)

  def messageHistory[F[_]: TelegramClient](db: MessageRepository): Scenario[IO, Unit] =
    for {
      msg <- Scenario.expect(textMessage)
      _   <- {
        Loggers.main.info(Caught, msg)
        Scenario.eval(db.addMessage(msg))
      }
    } yield ()

  def summarize[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("summarize"))
      _       <- Try(command.text.dropWhile(!_.equals(' ')).trim.toInt) match {
        case Success(value) => Scenario.eval(command.chat.send(Summarizer.summary(command, value, DB).unsafeRunSync()))
        case Failure(_) => Scenario.eval(command.chat.send("Argumento no válido, usa /summarize número"))
      }
    } yield ()

  def delete[F[_]: TelegramClient](db: MessageRepository): Scenario[IO, Unit] =
    for {
      command <- Scenario.expect(command("delete"))
      _       <- Scenario.eval(db.deleteMessage(command.chat.id))
    } yield ()

  def help[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      command <- Scenario.expect(command("help"))
      _       <- Scenario.eval(command.chat.send(Helper.display))
    } yield ()
}