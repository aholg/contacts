package aholg.solution

import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ContactsServiceTest extends AnyFunSuite with BeforeAndAfterAll with Matchers with MockFactory {

  test("Should return contacts of contacts") {
    val mockContactsRepository = mock[ContactsRepository]
    val service = new ContactsService(mockContactsRepository)

    (mockContactsRepository.contacts _)
      .expects(UserId(1))
      .returning(Future.successful(Seq(UserId(2), UserId(3))))

    (mockContactsRepository.contacts _)
      .expects(UserId(2))
      .returning(Future.successful(Seq.empty))

    (mockContactsRepository.contacts _)
      .expects(UserId(3))
      .returning(Future.successful(Seq(UserId(4))))

    val result = Await.result(service.contactsOfContacts(UserId(1)), 1.seconds)
    result shouldEqual Seq(UserId(4))
  }

  test("Should retrieve contacts of contacts without duplicates")(pending)

  test("Should retrieve contacts of contacts without entries existing in the original users contact list")(pending)

  test("Should retrieve no contacts of contacts if the user had no contacts")(pending)

  test("Should retrieve no contacts of contacts if the user's contacts had no contacts")(pending)

  test("Should retrieve no contacts of contacts if the user's contacts had no new contacts")(pending)

  test("Should retrieve contacts")(pending)

  test("Should update contacts")(pending)
}
