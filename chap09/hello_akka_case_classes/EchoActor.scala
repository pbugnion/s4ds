
import akka.actor._

object EchoActor { 
  case object EchoHello
  case class EchoMessage(val msg:String)

  def props:Props = Props[EchoActor]
}

class EchoActor extends Actor with ActorLogging {
  import EchoActor._ // import the message definitions
  def receive = {
    case EchoHello => log.info("hello")
    case EchoMessage(s) => log.info(s)  
  }
}
