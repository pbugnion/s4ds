
import akka.actor._
import akka.routing._
import scala.concurrent.duration._

object FetcherDemo extends App {
  import Fetcher._

  // Necessary for the scheduler
  import scala.concurrent.ExecutionContext.Implicits.global

  val system = ActorSystem("fetchers")

  val token = sys.env.get("GHTOKEN")

  val router = system.actorOf(RoundRobinPool(4).props(
    Fetcher.props(token))
  )

  List("odersky", "derekwyatt", "rkuhn", "tototoshi") foreach { login => 
    router ! Fetch(login) 
  }

  system.scheduler.scheduleOnce(2.seconds) { system.shutdown }

}
