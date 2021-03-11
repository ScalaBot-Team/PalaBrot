package palabrot.utils

import canoe.models.messages.UserMessage
import canoe.models.{Channel, Group, PrivateChat, Supergroup}
import com.typesafe.scalalogging.Logger

object Loggers {

  sealed trait LogEvent
  case object Caught extends LogEvent
  case object Stored extends LogEvent
  case object Deleted extends LogEvent
  case object Summary extends LogEvent

  sealed abstract class PalabrotLogger(val logger: Logger) {
    def info(event: LogEvent, logInfo: UserMessage): Unit = logger.info(s"Message $event ~ Chat ${
      (logInfo.chat match {
        case PrivateChat(_, username, _, _) => username
        case Group(_, title) => title
        case Supergroup(_, title, _) => title
        case Channel(_, title, _) => title
      }).getOrElse("unknown")
    } ~ User ${logInfo.from.get.firstName}")
  }

  val main: PalabrotLogger = new PalabrotLogger(Logger("Main logs")){}
  val db: PalabrotLogger = new PalabrotLogger(Logger("DB logs")){}
  val summary: PalabrotLogger = new PalabrotLogger(Logger("Summary logs")){}


}
