package palabrot.services

import canoe.models.messages.TextMessage
import cats.effect.IO
import palabrot.utils.Loggers
import palabrot.utils.Loggers.Summary

object Summarizer {

  def summary(command: TextMessage, messagesNumber: Int, db: MessageRepository): IO[String] = {
      Loggers.summary.info(Summary, command)
      //Code for return summary
      db.getMessagesFromChat(messagesNumber, command.chat).flatMap(l => IO.pure(l.mkString(" ")))
    }
}
