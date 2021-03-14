package palabrot.services

import canoe.models.messages.TextMessage
import cats.effect.IO
import palabrot.utils.Loggers
import palabrot.utils.Loggers.Summary

object Summarizer {
  
  trait DBService[F[_]] {
    def summary(command: TextMessage, messagesNumber: Int): F[String]
  }

  val elastic: DBService[IO] = (command: TextMessage, messagesNumber: Int) => {
    Loggers.summary.info(Summary, command)
    //Code for return summary
    IO.pure(s"Summary for $messagesNumber messages") //Or SummarizeArgumentsException
  }
}
