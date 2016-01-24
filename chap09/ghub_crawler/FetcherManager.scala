
import scala.collection.mutable

import akka.actor._

object FetcherManager {
  case class AddToQueue(val login:String)
  case object GiveMeWork

  def props(token:Option[String], nFetchers:Int) = 
    Props(classOf[FetcherManager], token, nFetchers)
}

class FetcherManager(val token:Option[String], val nFetchers:Int)
extends Actor with ActorLogging {

  import FetcherManager._

  // queue of usernames whose followers we need to fetch
  val fetchQueue = mutable.Queue.empty[String]

  // set of users we have already fetched. 
  val fetchedUsers = mutable.Set.empty[String]

  // Instantiate worker actors
  val followerExtractor = context.actorOf(
    FollowerExtractor.props(self))
  val responseInterpreter = context.actorOf(
    ResponseInterpreter.props(followerExtractor))
  val fetchers = (0 until nFetchers).map { i =>
    context.actorOf(
      Fetcher.props(token, self, responseInterpreter))
  }

  // receive method when the actor has work:
  // If we receive additional work, we just push it onto the
  // queue.
  // If we receive a request for work from a Fetcher,
  // we pop an item off the queue. If that leaves the 
  // queue empty, we transition to the 'receiveWhileEmpty'
  // method.
  def receiveWhileNotEmpty:Receive = {
    case AddToQueue(login) => queueIfNotFetched(login)
    case GiveMeWork =>
      val login = fetchQueue.dequeue
      // send a Fetch message back to the sender.
      // we can use the `sender` method to reply to a message
      sender ! Fetcher.Fetch(login)
      if (fetchQueue.isEmpty) { 
        context.become(receiveWhileEmpty)
      }
  }

  // receive method when the actor has no  work:
  // if we receive work, we add it onto the queue, transition
  // to a state where we have work, and notify the fetchers
  // that work is available.
  def receiveWhileEmpty:Receive = {
    case AddToQueue(login) =>
      queueIfNotFetched(login)
      context.become(receiveWhileNotEmpty)
      fetchers.foreach { _ ! Fetcher.WorkAvailable }
    case GiveMeWork => // do nothing
  }

  // Start with an empty queue.
  def receive = receiveWhileEmpty

  def queueIfNotFetched(login:String) {
    if (! fetchedUsers(login)) {
      log.info(s"Pushing $login onto queue") 
      // or do something useful...
      fetchQueue += login
      fetchedUsers += login
    }
  }

}
