import akka.actor._
import scalaj.http._
import scala.concurrent.Future

object Fetcher {
  case class Fetch(url:String)
  case object WorkAvailable

  def props(
    token:Option[String], 
    fetcherManager:ActorRef, 
    responseInterpreter:ActorRef):Props =
      Props(classOf[Fetcher], 
        token, fetcherManager, responseInterpreter)
}

class Fetcher(
  val token:Option[String], 
  val fetcherManager:ActorRef,
  val responseInterpreter:ActorRef) 
extends Actor with ActorLogging {
  import Fetcher._
  import context.dispatcher

  def receive = {
    case Fetch(login) => fetchFollowers(login)
    case WorkAvailable => 
      fetcherManager ! FetcherManager.GiveMeWork
  }

  private def fetchFollowers(login:String) {
    val unauthorizedRequest = Http(
      s"https://api.github.com/users/$login/followers")
    val authorizedRequest = token.map { t =>
      unauthorizedRequest.header("Authorization", s"token $t")
    }
    val request = authorizedRequest.getOrElse(unauthorizedRequest)
    val response = Future { request.asString }

    response.onComplete { r =>
      responseInterpreter ! 
        ResponseInterpreter.InterpretResponse(login, r)
      fetcherManager ! FetcherManager.GiveMeWork
    }
  }

}

