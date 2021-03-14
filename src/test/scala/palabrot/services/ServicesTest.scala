package palabrot.services
import canoe.models.{Group, User}
import canoe.models.messages.TextMessage
import munit.CatsEffectSuite

class ServicesTest extends CatsEffectSuite {

  val sampleUser = User(1,false,"Test Name", None, None, None, None, None, None)
  val sampleGroup = Group(1,Some("Test Group"))
  val sampleMessage = TextMessage(1, sampleGroup,1,"Test Message",None,Some(sampleUser))

  test("MessageCleaner returns IO[Boolean] when is required to delete messages history") {
    MessageCleaner.elastic.deleteMessages(sampleMessage).assertEquals(true)
  }

  test("Summarizer returns summary from n(100) messages") {
    Summarizer.elastic.summary(sampleMessage,100).assertEquals("Summary for 100 messages")
  }

  test("Storage returns IO[Boolean] when a message is stored succesfuly") {
    Storage.elastic.addMessage(sampleMessage).assertEquals(true)
  }
}
