
import akka.actor._

class Echo extends Actor with ActorLogging {
  def receive = {
    case msg:String => { Thread.sleep(1000) ; log.info(s"Received '$msg'") }
  }
}
