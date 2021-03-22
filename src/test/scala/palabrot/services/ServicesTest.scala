package palabrot.services
import canoe.models.{Chat, ChatId, Group, User}
import canoe.models.messages.TextMessage
import cats.effect.IO
import munit.CatsEffectSuite

class ServicesTest extends CatsEffectSuite {

  val testUser = User(1, false, "Test Name", None, None, None, None, None, None)
  val testGroup = Group(1, Some("Test Group"))
  val testMessage = TextMessage(1, testGroup, 1, "Test Message", None, Some(testUser))

  val testDB: MessageRepository = new MessageRepository {
    override def addMessage(message: TextMessage): IO[Boolean] = IO.pure(true)
    override def getMessagesFromChat(num: Int, chat: Chat): IO[List[String]] = chat match {
      case Group(_, Some(title)) => IO.pure (s"Summary of $num messages from chat $title" :: Nil)
      case _ => IO.pure("Test failed" :: Nil)
    }
    override def deleteMessage(chatId: ChatId): IO[Boolean] = IO.pure(true)
  }


  test("Summarizer returns summary from n(100) messages from Test Group") {
    Summarizer.summary(testMessage,100, testDB)
      .assertEquals("Summary of 100 messages from chat Test Group")
  }

}
