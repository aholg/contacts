package aholg.solution

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

class ContactsActorTest extends AsyncFunSuite with BeforeAndAfterAll with Matchers {
  val testKit = ActorTestKit()

  override def afterAll(): Unit = testKit.shutdownTestKit()

  test("Should return 0 contacts for user without contacts") {
    val system = testKit.spawn(ContactsActor(), "contractsActorTest")
    val contactsActor = new ContactsActor(system)(scheduler = testKit.scheduler)

    contactsActor.contacts(UserId(1)).map { result =>
      result shouldEqual Seq.empty
    }
  }

  test("Should return an exception in case of failure")(pending)
}
