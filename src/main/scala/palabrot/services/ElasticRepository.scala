package palabrot.services

import canoe.models.messages.TextMessage
import canoe.models.{Chat, ChatId}
import cats.effect.IO
import com.sksamuel.elastic4s.{ElasticClient, Indexable}


class ElasticRepository(client: ElasticClient, index: String) extends MessageRepository {

  import com.sksamuel.elastic4s.ElasticDsl._

  implicit object MessageIndexable extends Indexable[TextMessage] {
    override def json(t: TextMessage): String =
      s""" { "chatId" : "${t.chat.id}", "text" : "${t.text}", "date" : "${t.date}", "user" : "${t.from.getOrElse("unknown")}" } """
  }

  override def addMessage(message: TextMessage): IO[Boolean] = {
    client.execute(indexInto(index).doc(message))
    IO.pure(true)
  }

  override def getMessagesFromChat(num: Int, chat: Chat): IO[List[String]] =
    IO.pure(client.execute(get("chatId", s"${chat.id}")).mapTo[String].value.get.get :: Nil)

  override def deleteMessage(chatId: ChatId): IO[Boolean] = {
    client.execute(deleteById("chatId", s"$chatId"))
    IO.pure(true)
  }
}
