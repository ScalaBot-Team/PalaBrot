package palabrot.resources

import canoe.models.messages.UserMessage
import canoe.models.{Channel, Group, PrivateChat, Supergroup}
import com.typesafe.scalalogging.Logger

object Loggers {
  trait PalabrotLogger {
    def info(event: String, logInfo: UserMessage): Unit = print("Log") //Default function
  }

  val main: PalabrotLogger = new PalabrotLogger {
    val mainLogger = Logger("Main logs")

    override def info(event: String, logInfo: UserMessage): Unit = mainLogger.info(s"Message caught from chat ${
      (logInfo.chat match {
        case PrivateChat(_, username, _, _) => username
        case Group(_, title) => title
        case Supergroup(_, title, _) => title
        case Channel(_, title, _) => title
      }).getOrElse("unknown")
    } written by ${logInfo.from.get.firstName}")
  }

  val db = new PalabrotLogger {
    val dbLogger = Logger("Main logs")

    override def info(event: String, logInfo: UserMessage): Unit =
      dbLogger.info(s"Adding message to DB from chat ${
        (logInfo.chat match {
          case PrivateChat(_, username, _, _) => username
          case Group(_, title) => title
          case Supergroup(_, title, _) => title
          case Channel(_, title, _) => title
        }).getOrElse("unknown")
      } written by ${logInfo.from.get.firstName}")
  }
}
