package aholg.solution

import scala.concurrent.Future

trait ContactsRepository {
  def contacts(userId: UserId): Future[Seq[UserId]]

  def updateContacts(userId: UserId, contacts: Seq[UserId]): Future[Unit]
}

case class UserId(value: Int)