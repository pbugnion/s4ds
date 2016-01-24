
import scala.collection.mutable
import scala.util._

import scalaj.http._
import org.json4s._
import org.json4s.native.JsonMethods._

object GhubSingle extends App {
  val seedUser = "odersky" // the origin of the network

  // Users whose URLs need to be fetched
  val queue = mutable.Queue(seedUser)

  // Set of users that we have already fetched
  // (to avoid re-fetching them)
  val fetchedUsers = mutable.Set.empty[String]

  lazy val token = sys.env.get("GHTOKEN")

  /** Return a list of followers for user `login` */
  def fetchFollowersForUser(login:String):List[String] = {
    val unauthorizedRequest = Http(
      s"https://api.github.com/users/$login/followers")
    val authorizedRequest = token.map { t =>
      unauthorizedRequest.header("Authorization", s"token $t")
    }

    val request = authorizedRequest.getOrElse(unauthorizedRequest)

    val response = Try { request.asString }
    val followers = interpret(login, response)
    followers
  }

  /** Extract list of followers from response. */
  private def interpret(
    login:String, response:Try[HttpResponse[String]]
  ):List[String] = response match {
    case Success(r) => responseToJson(r.body) match {
      case Success(jsonResponse) => extractFollowers(jsonResponse)
      case Failure(e) =>
        println(s"Error parsing response to JSON for $login: $e")
        List.empty[String]
    }
    case Failure(e) =>
      println(s"Error fetching URL for $login: $e")
      List.empty[String]
  }

  /** Parse response body and return JArray of followers */
  private def responseToJson(responseBody:String):Try[JArray] = {
    val jvalue = Try { parse(responseBody) }
    jvalue.flatMap {
      case a:JArray => Success(a)
      case _ => Failure(new IllegalStateException(
        "Incorrectly formatted JSON: not an array"))
    }
  }

  /** Extract a list of followers from JArray */
  private def extractFollowers(followerArray:JArray):JArray = for {
      JObject(follower) <- followerArray
      JField("login", JString(login)) <- follower
    } yield login

  // Run the crawler
  while (queue.nonEmpty) {
    val user = queue.dequeue
    println(s"Dequeued $user")
    if (!fetchedUsers(user)) {
      println(s"Fething followers for $user")
      val followers = fetchFollowersForUser(user)
      followers foreach { follower =>
        // add the follower to queue of people whose
        // followers we want to find.
        queue += follower
      }
      fetchedUsers += user
    }
  }
}
