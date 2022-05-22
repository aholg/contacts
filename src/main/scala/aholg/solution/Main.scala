package aholg.solution

import akka.actor.typed.{ActorSystem, Scheduler}
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  implicit val system: ActorSystem[ContactsActor.ContactsCommand] = ActorSystem(ContactsActor(), "ContactsActor")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val scheduler: Scheduler = system.scheduler

  val contactsActor = new ContactsActor(system)
  val contactsService = new ContactsService(contactsActor)
  val contactsController = new ContactsController(contactsService)

  val bindingFuture = Http().newServerAt("localhost", 9000).bind(contactsController.route)
}
