
import akka.actor._

import org.json4s._
import org.json4s.native.JsonMethods._

object FollowerExtractor {
  
  // Messages
  case class Extract(val login:String, val jsonResponse:JArray)

  // Props factory method
  def props = Props[FollowerExtractor]
}

class FollowerExtractor extends Actor with ActorLogging {
  import FollowerExtractor._
  def receive = {
    case Extract(login, followerArray) => {
      val followers = extractFollowers(followerArray)
      log.info(s"$login -> ${followers.mkString(", ")}")
    }
  }

  def extractFollowers(followerArray:JArray) = for {
    JObject(follower) <- followerArray
    JField("login", JString(login)) <- follower
  } yield login

}
