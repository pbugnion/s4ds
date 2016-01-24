
import akka.actor._
import org.json4s._
import org.json4s.native.JsonMethods._

object FollowerExtractor {
  
  // messages
  case class Extract(val login:String, val jsonResponse:JArray)

  // props factory method
  def props(manager:ActorRef) = 
    Props(classOf[FollowerExtractor], manager)
}

class FollowerExtractor(manager:ActorRef)
extends Actor with ActorLogging {
  import FollowerExtractor._

  def receive = {
    case Extract(login, followerArray) =>
      val followers = extractFollowers(followerArray)
      followers foreach { f => 
        manager ! FetcherManager.AddToQueue(f) 
      }
  }

  def extractFollowers(followerArray:JArray) = for {
    JObject(follower) <- followerArray
    JField("login", JString(login)) <- follower
  } yield login

}
