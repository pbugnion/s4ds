
import akka.actor._

object HelloAkka extends App {

  val system = ActorSystem("HelloActors")

  val echo1 = system.actorOf(Props[Echo], name="echo1")
  val echo2 = system.actorOf(Props[Echo], name="echo2")
  echo1 ! "hello echo1"
  echo2 ! "hello echo2"
  echo1 ! "bye bye"

  Thread.sleep(2000)
  system.shutdown

}
