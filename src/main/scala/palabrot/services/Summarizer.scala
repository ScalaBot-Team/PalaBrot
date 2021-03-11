package palabrot.services

import canoe.models.messages.TextMessage
import palabrot.utils.Loggers
import palabrot.utils.Loggers.Summary

object Summarizer {
  def summary(command: TextMessage): String = {
    Loggers.db.info(Summary, command)
    //Code for return summary
    "Summary" //Or SummarizeArgumentsException
  }
}
