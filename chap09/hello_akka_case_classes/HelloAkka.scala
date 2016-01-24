import akka.actor._

object HelloAkka extends App {

  // We need an actor system before we can 
  // instantiate actors
  val system = ActorSystem("HelloActors")

  // instantiate our two actors
  val echo1 = system.actorOf(EchoActor.props, name="echo1")
  val echo2 = system.actorOf(EchoActor.props, name="echo2")

  // Send them messages. We do this using the "!" operator
  echo1 ! EchoActor.EchoHello
  echo2 ! EchoActor.EchoMessage("We're learning Akka.")

  // Give the actors time to process their messages, 
  // then shut the system down to terminate the program
  Thread.sleep(500)
  system.shutdown
}
