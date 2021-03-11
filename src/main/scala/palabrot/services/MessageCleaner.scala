package palabrot.services

import canoe.models.messages.TextMessage
import cats.effect.IO
import palabrot.utils.Loggers
import palabrot.utils.Loggers.Deleted

object MessageCleaner {

  trait DBService[F[_]] {
    def deleteMessages(message: TextMessage): F[Boolean]
  }

  val elastic: DBService[IO] = (command: TextMessage) => {
    Loggers.db.info(Deleted, command)
    //Code for delete data from db
    IO.pure(true) //or false
  }
}
