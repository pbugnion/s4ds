import akka.actor._
import scala.util._

import scalaj.http._
import org.json4s._
import org.json4s.native.JsonMethods._

object ResponseInterpreter {

  // Messages
  case class InterpretResponse(
    login:String, response:Try[HttpResponse[String]]
  )

  // Props factory
  def props(followerExtractor:ActorRef) = 
    Props(classOf[ResponseInterpreter], followerExtractor)
}

class ResponseInterpreter(followerExtractor:ActorRef)
extends Actor with ActorLogging {
  // Import the message definitions
  import ResponseInterpreter._

  def receive = {
    case InterpretResponse(login, r) => interpret(login, r)
  }

  // If the query was successful, extract the JSON response
  // and pass it onto the follower extractor.
  // If the query failed, or is badly formatted, throw an error
  // We should also be checking error codes here.
  private def interpret(
    login:String, response:Try[HttpResponse[String]]
  ) = response match {
    case Success(r) => responseToJson(r.body) match {
      case Success(jsonResponse) => 
        followerExtractor ! FollowerExtractor.Extract(
          login, jsonResponse)
      case Failure(e) => 
        log.error(
          s"Error parsing response to JSON for $login: $e")
    }
    case Failure(e) => log.error(
      s"Error fetching URL for $login: $e")
  }

  // Try and parse the response body as JSON. 
  // If successful, coerce the `JValue` to a `JArray`.
  private def responseToJson(responseBody:String):Try[JArray] = {
    val jvalue = Try { parse(responseBody) }
    jvalue.flatMap { 
      case a:JArray => Success(a)
      case _ => Failure(new IllegalStateException(
        "Incorrectly formatted JSON: not an array"))
    }
  }
}
