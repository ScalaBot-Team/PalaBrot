package palabrot.services

import canoe.models.messages.TextMessage
import palabrot.utils.Loggers
import palabrot.utils.Loggers.Summary

object Summarizer {
  def summary(command: TextMessage, messagesNumber: Int): String = {
    Loggers.summary.info(Summary, command)
    //Code for return summary
    s"Summary for $messagesNumber messages" //Or SummarizeArgumentsException
  }
}
