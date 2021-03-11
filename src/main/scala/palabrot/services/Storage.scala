package palabrot.services

import canoe.models.messages.TextMessage
import cats.effect.IO
import palabrot.utils.Loggers
import palabrot.utils.Loggers.Stored

object Storage {

  trait DBService[F[_]] {
    def addMessage(message: TextMessage): F[Boolean]
  }

  val elastic: DBService[IO] = (message: TextMessage) => {
    Loggers.db.info(Stored, message)
    //Code for add data to db
    IO.pure(true) //or false
  }

}
