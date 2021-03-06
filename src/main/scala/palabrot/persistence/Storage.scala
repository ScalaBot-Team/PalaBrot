package palabrot.persistence

import canoe.models.messages.TextMessage
import cats.effect.IO
import palabrot.resources.Loggers

object Storage {

  trait StorageService[F[_]] {
    def addMessage(message: TextMessage): F[Boolean]
    def deleteMessages(message: TextMessage): F[Boolean]
  }

  val elastic: StorageService[IO] = new StorageService[IO] {

    override def addMessage(message: TextMessage): IO[Boolean] = {
      Loggers.db.info("event", message)
      //Code for add data to db
      IO.pure(true) //or false
    }

    override def deleteMessages(message: TextMessage): IO[Boolean] = {
      Loggers.db.info("event", message)
      //Code for delete data from db
      IO.pure(true) //or false
    }
  }


}
