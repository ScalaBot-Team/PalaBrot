package palabrot

import canoe.models.messages.TextMessage
import canoe.models._
import cats.effect.IO
import cats.effect.concurrent.Ref
import com.typesafe.scalalogging.Logger

object storageService {

  final class FakeDataBase(ref: Ref[IO, Map[Chat, List[String]]]) {
    def addMessageToDB(chat: Chat, newMessage: String): IO[Unit] = {
      ref.update(_.updatedWith(chat)(messageList => messageList match {
        case Some(l) => Some(l.prepended(newMessage))
        case None => Some(List(newMessage))
      }))
    }

    def getMessagesFromDB(chat: Chat, num: Int): IO[List[String]] =
      ref.get.map(_.getOrElse(chat, List("Chat not found")).take(num))
  }

  trait Service[F[_]] {
    def getMessages(chat: Chat, num: Int): F[List[String]]

    def addMessage(message: TextMessage): F[Unit]
  }

  val db: IO[FakeDataBase] = Ref[IO].of(Map.empty[Chat, List[String]]).map(new FakeDataBase(_))
  val logger = Logger("DB logs")
  /** We need store messages and get n messages from db */
  val store: Service[IO] = new Service[IO] {
    def getMessages(chat: Chat, num: Int): IO[List[String]] = db.unsafeRunSync().getMessagesFromDB(chat, num)

    def addMessage(message: TextMessage): IO[Unit] = if (!message.text.startsWith("/")) {
      logger.info(s"Adding message to DB from chat ${
        (message.chat match {
          case PrivateChat(_, username, _, _) => username
          case Group(_, title) => title
          case Supergroup(_, title, _) => title
          case Channel(_, title, _) => title
        }).getOrElse("unknown")
      } written by ${message.from.get.firstName}")
      db.unsafeRunSync().addMessageToDB(message.chat, message.text)
    } else IO.unit
  }
}