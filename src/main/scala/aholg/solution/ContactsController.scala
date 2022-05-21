package aholg.solution

import akka.http.scaladsl.server.{Directives, Route}
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ContactsController(contactsService: ContactsService)(implicit ec: ExecutionContext) extends Directives {

  import ContactsController._

  val route: Route =
    concat(
      get {
        path("user" / IntNumber / "sd-contacts") { userId =>
          complete(contactsService.contactsOfContacts(UserId(userId)))
        }
      },
      post {
        path("user" / IntNumber / "contacts") { userId =>
          entity(as[ContactsRequest]) { request =>
            onComplete(contactsService.updateContacts(UserId(userId), request.contacts)) {
              case Success(()) => complete(StatusCodes.Created)
              case Failure(_) =>
                complete(HttpResponse(StatusCodes.InternalServerError, entity = "Something went wrong while updating contacts"))
            }
          }
        }
      }
    )
}

object ContactsController extends SprayJsonSupport with DefaultJsonProtocol {
  case class ContactsRequest(contacts: Seq[UserId])

  implicit val userIdWriteFormat: RootJsonFormat[UserId] = new RootJsonFormat[UserId] {
    def write(u: UserId): JsNumber = JsNumber(u.value)

    override def read(json: JsValue): UserId = json match {
      case JsNumber(n) => UserId(n.toInt)
      case _ => deserializationError("Color expected")
    }
  }

  implicit val contactsRequestFormat: RootJsonFormat[ContactsRequest] = jsonFormat1(ContactsRequest)
}
