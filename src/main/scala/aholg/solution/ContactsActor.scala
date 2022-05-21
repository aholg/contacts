package aholg.solution

import aholg.solution.ContactsActor.{ContactsCommand, GetContacts}
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, Scheduler}
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

class ContactsActor(actor: ActorRef[ContactsCommand])
                   (implicit val timeout: Timeout = Timeout(5.seconds), scheduler: Scheduler) extends ContactsRepository {

  override def contacts(userId: UserId): Future[Seq[UserId]] = {
    actor.ask[Seq[UserId]](ref => GetContacts(userId, ref))(timeout = timeout, scheduler = scheduler)
  }
}

private object ContactsActor {

  sealed trait ContactsCommand

  final case class GetContacts(userId: UserId, replyTo: ActorRef[Seq[UserId]]) extends ContactsCommand

  def apply(): Behavior[ContactsCommand] = Behaviors.receiveMessage[ContactsCommand] {
    case GetContacts(_, ref) =>
      ref ! Seq.empty
      Behaviors.same
  }
}