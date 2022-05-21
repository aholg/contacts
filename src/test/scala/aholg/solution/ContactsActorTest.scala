package aholg.solution

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

class ContactsActorTest extends AsyncFunSuite with BeforeAndAfterAll with Matchers {
  val testKit: ActorTestKit = ActorTestKit()

  test("Should return 0 contacts for user without contacts") {
    val actor = testKit.spawn(ContactsActor(), "contractsActorTest")
    val contactsActor = new ContactsActor(actor)(scheduler = testKit.scheduler)

    contactsActor.contacts(UserId(1)).map { result =>
      result shouldEqual Seq.empty
    }.andThen(_ => testKit.stop(actor))
  }

  test("Should update contacts for a user") {
    val actor = testKit.spawn(ContactsActor(), "contractsActorTest")
    val contactsActor = new ContactsActor(actor)(scheduler = testKit.scheduler)
    val userId = UserId(1)
    val contacts = Seq(UserId(2), UserId(3))

    contactsActor.updateContacts(userId, contacts).flatMap { _ =>
      contactsActor.contacts(userId).map { result =>
        result shouldEqual contacts
      }
    }.andThen(_ => testKit.stop(actor))
  }

  test("Should overwrite old contacts when updating")(pending)

  test("Should return an exception in case of failure when retrieving contacts")(pending)

  test("Should return an exception in case of failure when updating contacts")(pending)
}
