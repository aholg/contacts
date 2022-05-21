package aholg.solution

import aholg.solution.ContactsActor.{ContactsCommand, GetContacts, UpdateContacts}
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, Scheduler}
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration._

class ContactsActor(actor: ActorRef[ContactsCommand])
                   (implicit val timeout: Timeout = Timeout(5.seconds), scheduler: Scheduler) extends ContactsRepository {

  override def contacts(userId: UserId): Future[Seq[UserId]] = {
    actor.ask[Seq[UserId]](ref => GetContacts(userId, ref))(timeout = timeout, scheduler = scheduler)
  }

  override def updateContacts(userId: UserId, contacts: Seq[UserId]): Future[Unit] = {
    actor.ask[Unit](ref => UpdateContacts(userId, contacts, ref))(timeout = timeout, scheduler = scheduler)
  }
}

object ContactsActor {

  sealed trait ContactsCommand

  final case class GetContacts(userId: UserId, replyTo: ActorRef[Seq[UserId]]) extends ContactsCommand

  final case class UpdateContacts(userId: UserId, contacts: Seq[UserId], replyTo: ActorRef[Unit]) extends ContactsCommand

  val inMemoryContacts = mutable.Map.empty[UserId, Seq[UserId]]

  def apply(): Behavior[ContactsCommand] = Behaviors.receiveMessage[ContactsCommand] {
    case GetContacts(userId, ref) =>
      ref ! inMemoryContacts.getOrElse(userId, Seq.empty)
      Behaviors.same
    case UpdateContacts(userId, contacts, ref) =>
      inMemoryContacts.addOne(userId, contacts)
      ref ! ()
      Behaviors.same
  }
}