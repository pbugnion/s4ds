
import akka.actor._
import scalaj.http._
import scala.concurrent.Future

/** Fetcher companion object.
 *
 * This normally includes a case class / object for all the messages
 * an actor can receive, and, if the actor constructor takes
 * parameters, a factory method for constructing the actor.
 */
object Fetcher {
  // message definitions
  case class Fetch(val login:String)

  // Props factory definitions
  def props(token:Option[String]):Props = 
    Props(classOf[Fetcher], token)
  def props():Props = Props(classOf[Fetcher], None)
}

class Fetcher(val token:Option[String])
extends Actor with ActorLogging {
  import Fetcher._ // import message definition

  // We will need an execution context for the future. 
  // The dispatcher doubles up as context, so we need
  // to import it.
  import context.dispatcher

  def receive = {
    case Fetch(login) => fetchUrl(login)
  }

  private def fetchUrl(login:String) {
    val unauthorizedRequest = Http(
      s"https://api.github.com/users/$login/followers")
    val authorizedRequest = token.map { t =>
      unauthorizedRequest.header("Authorization", s"token $t")
    }

    // Prepare the request: try to use the authorized request
    // if a token was given, and fall back on an unauthorized 
    // request
    val request = authorizedRequest.getOrElse(unauthorizedRequest)

    // Fetch from github
    // By wrapping this in a future, we return execution to the 
    // receive method straight-away, making our actors more responsive.
    val response = Future { request.asString }
    response.onComplete { r =>
      log.info(s"Response from $login: $r")
    }
  }

}
