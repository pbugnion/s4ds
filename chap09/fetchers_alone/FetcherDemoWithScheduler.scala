
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import akka.actor._

object FetcherDemoWithScheduler extends App {
  import Fetcher._

  val system = ActorSystem("fetchers")

  // Read the github token if present.
  val token = sys.env.get("GHTOKEN")

  val fetchers = (0 until 4).map { i =>
    system.actorOf(Fetcher.props(token))
  }

  fetchers(0) ! Fetch("odersky")
  fetchers(1) ! Fetch("derekwyatt")
  fetchers(2) ! Fetch("rkuhn")
  fetchers(3) ! Fetch("tototoshi")

  system.scheduler.scheduleOnce(5.seconds) { system.shutdown }

}
