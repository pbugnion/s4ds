
import scala.io._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.util._

import org.json4s._
import org.json4s.native.JsonMethods._

import scalaj.http._

object GitHubUserAuth {

  implicit val formats = DefaultFormats

  case class User(
    val id:Long,
    val username:String
  )


  def fetchUserFromUrl(url:String):Future[User] = {
    val request = Http(url)
    val token = "" // Your token here
    request.header("Authentication", s"token $token")
    val response = Future { request.asString.body }
    val parsedResponse = response.map { r => parse(r) }
    parsedResponse.flatMap(extractUser)
  }

  def extractUser(jsonResponse:JValue):Future[User] = Future {
    val o = jsonResponse.transformField {
      case ("login", name) => ("username", name)
    }
    o.extract[User]
  }

  def main(args:Array[String]) {
    val names = args.toList
    val users = for {
      name <- names
      url = s"https://api.github.com/users/$name"
      user = fetchUserFromUrl(url)
    } yield user

    names.zip(users).foreach { case(name, user) =>
      user.onComplete {
        case Success(u) => println(s" ** Extracted for $name: $u")
        case Failure(e) => println(s" ** Error fetching user $name: $e")
      }
    }

    Await.ready(Future.sequence(users), 1 minute)

  }

}
