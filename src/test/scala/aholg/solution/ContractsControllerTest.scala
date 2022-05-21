package aholg.solution

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class ContractsControllerTest extends AnyFunSuite
  with BeforeAndAfterAll
  with Matchers
  with MockFactory
  with ScalatestRouteTest {

  import ContactsController._

  test("Should return contacts of contacts") {
    val mockContactsService = mock[ContactsService]
    (mockContactsService.contactsOfContacts _)
      .expects(UserId(1))
      .returning(Future.successful(Set(UserId(2), UserId(3))))

    val controller = new ContactsController(mockContactsService)

    Get("/user/1/sd-contacts") ~> controller.route ~> check {
      responseAs[Seq[UserId]] shouldEqual Seq(UserId(2), UserId(3))
    }
  }

  test("Should update contacts") {
    val mockContactsService = mock[ContactsService]
    (mockContactsService.updateContacts _)
      .expects(UserId(1), Seq(UserId(2), UserId(3), UserId(4), UserId(5)))
      .returning(Future.successful(()))

    val controller = new ContactsController(mockContactsService)

    Post("/user/1/contacts", ContactsRequest(Seq(UserId(2), UserId(3), UserId(4), UserId(5)))) ~> controller.route ~> check {
      status shouldEqual StatusCodes.Created
    }
  }

  test("Should return error response if updating contacts failed")(pending)

  test("Should return error response if retrieving contacts failed")(pending)
}
