import canoe.models.messages.TextMessage
import canoe.models.{Group, User}
import munit.CatsEffectSuite
import palabrot.MainFlow


class MainFlowTest extends CatsEffectSuite {
  val testUser = User(1, false, "Test Name", None, None, None, None, None, None)
  val testGroup = Group(1, Some("Test Group"))
  val testMessage = TextMessage(1, testGroup, 1, "Test Message", None, Some(testUser))

  test("When delete messages gets true") {
    MainFlow.DB.deleteMessage(testMessage.chat.id).assert
  }
  test("Summarizer returns summary from n(100) messages from Test Group") {
    MainFlow.DB.addMessage(testMessage).assert
  }

}