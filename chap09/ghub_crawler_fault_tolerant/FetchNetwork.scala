import akka.actor._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object FetchNetwork extends App {

  // Get token if exists
  val token = sys.env.get("GHTOKEN")

  val system = ActorSystem("GithubFetcher")
  val manager = system.actorOf(FetcherManager.props(token, 2))

  manager ! FetcherManager.AddToQueue("odersky")

  system.scheduler.scheduleOnce(30.seconds) { system.shutdown }

}
