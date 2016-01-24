import akka.actor._
import scalaj.http._
import scala.concurrent.Future

object Fetcher {
  // message definitions
  case class Fetch(val login:String)

  // Props factory definitions
  def props(
      token:Option[String], responseInterpreter:ActorRef
  ):Props = Props(classOf[Fetcher], token, responseInterpreter)
}

class Fetcher(val token:Option[String], val responseInterpreter:ActorRef)
extends Actor with ActorLogging {

  import Fetcher._ // import message definition
  import context.dispatcher

  def receive = {
    case Fetch(login) => fetchFollowers(login)
  }

  private def fetchFollowers(login:String) {
    val unauthorizedRequest = Http(
      s"https://api.github.com/users/$login/followers")
    val authorizedRequest = token.map { t =>
      unauthorizedRequest.header("Authorization", s"token $t")
    }

    val request = authorizedRequest.getOrElse(unauthorizedRequest)
    val response = Future { request.asString }

    // Wrap the response in an InterpretResponse message and
    // forward it to the interpreter.
    response.onComplete { r =>
      responseInterpreter !
        ResponseInterpreter.InterpretResponse(login, r)
    }
  }
}
