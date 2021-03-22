package palabrot.services
import canoe.models.{Chat, ChatId}
import canoe.models.messages.TextMessage
import cats.effect.IO

trait MessageRepository{
    def addMessage(message: TextMessage): IO[Boolean]
    def getMessagesFromChat(num: Int, chat: Chat): IO[List[String]]
    def deleteMessage(chatId: ChatId): IO[Boolean]
}


