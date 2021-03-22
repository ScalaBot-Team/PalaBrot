package palabrot.services
import canoe.models.{Chat, ChatId}
import canoe.models.messages.TextMessage
import cats.effect.IO

trait MessageRepository{
    def addMessage(message: TextMessage): IO[Unit]
    def getMessagesFromChat(num: Int, chat: Chat): IO[String]
    def deleteMessage(chatId: ChatId): IO[Unit]
}


