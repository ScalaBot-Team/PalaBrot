package palabrot

import canoe.models.messages.TextMessage
import canoe.models._
import cats.effect.IO
import com.typesafe.scalalogging.Logger

object storageService {

  trait Service[F[_]] {
    def getMessages(chat: Chat, num: Int): F[List[String]]
    def addMessage(message: TextMessage): F[Unit]
  }

  var db = Map.empty[Chat, List[String]]
  val logger = Logger("DB logs")

  /** We need store messages and get n messages from db */
  val store: Service[IO] = new Service [IO] {
    def getMessages(chat: Chat, num: Int): IO[List[String]] =
      IO.pure(db.getOrElse(chat, List("Chat not found")).take(num))
    def addMessage(message: TextMessage): IO[Unit] = {
      logger.info(s"Adding message to DB from chat ${
        (message.chat match {
          case PrivateChat(_, username, _, _) => username
          case Group(_, title) => title
          case Supergroup(_, title, _) => title
          case Channel(_, title, _) => title
        }).getOrElse("unknown")
      } written by ${message.from.get.firstName}")
      if (!message.text.startsWith("/")) db = db.updatedWith(message.chat)(messageList => messageList match {
        case Some(l) => Some(l.prepended(message.text))
        case None => Some(List(message.text))
      })
      IO.unit
    }
  }
}
