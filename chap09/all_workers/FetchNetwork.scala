import akka.actor._
import akka.routing._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object FetchNetwork extends App {

  import Fetcher._ // Import messages and factory method

  // Get token if exists
  val token = sys.env.get("GHTOKEN")

  val system = ActorSystem("fetchers")

  // Instantiate actors
  val followerExtractor = system.actorOf(FollowerExtractor.props)
  val responseInterpreter = 
    system.actorOf(ResponseInterpreter.props(followerExtractor))

  val router = system.actorOf(RoundRobinPool(4).props(
    Fetcher.props(token, responseInterpreter))
  )

  List("odersky", "derekwyatt", "rkuhn", "tototoshi") foreach {
    login => router ! Fetch(login)
  }

  // schedule a shutdown
  system.scheduler.scheduleOnce(25.seconds) { system.shutdown }

}
