package aholg.solution

import scala.concurrent.Future

trait ContactsRepository {
  def contacts(userId: UserId): Future[Seq[UserId]]
}

case class UserId(value: Int)