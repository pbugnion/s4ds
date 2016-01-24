
import akka.actor._

import scala.collection.mutable
import scala.io.Source 
import scala.util._
import java.io._

object FetcherManager {
  case class AddToQueue(val login:String)
  case object GiveMeWork

  def props(token:Option[String], nFetchers:Int) = 
    Props(classOf[FetcherManager], token, nFetchers)

  val fetchedUsersFileName = "fetched-users.txt"
  val fetchQueueFileName = "fetch-queue.txt"
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

  // pre-start method: load saved state from text files 
  override def preStart {
    log.info("Running pre-start on fetcher manager")

    loadFetchedUsers
    log.info(
      s"Read ${fetchedUsers.size} visited users from source"
    )

    loadFetchQueue
    log.info(
      s"Read ${fetchQueue.size} users in queue from source"
    )

    // If the saved state contains a non-empty queue, 
    // alert the fetchers so they can start working.
    if (fetchQueue.nonEmpty) {
      context.become(receiveWhileNotEmpty)
      fetchers.foreach { _ ! Fetcher.WorkAvailable }
    }

  }

  // Dump the current state of the manager
  override def postStop {
    log.info("Running post-stop on fetcher manager")
    saveFetchedUsers
    saveFetchQueue
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

  // Helper methods to load from and write to files
  def loadFetchedUsers {
    val fetchedUsersSource = Try { 
      Source.fromFile(fetchedUsersFileName) 
    }
    fetchedUsersSource.foreach { s =>
      try s.getLines.foreach { l => fetchedUsers += l }
      finally s.close
    }
  }

  def loadFetchQueue {
    val fetchQueueSource = Try { 
      Source.fromFile(fetchQueueFileName) 
    }
    fetchQueueSource.foreach { s =>
      try s.getLines.foreach { l => fetchQueue += l }
      finally s.close
    }
  }

  def saveFetchedUsers {
    val fetchedUsersFile = new File(fetchedUsersFileName)
    val writer = new BufferedWriter(
      new FileWriter(fetchedUsersFile))
    fetchedUsers.foreach { user => writer.write(user + "\n") }
    writer.close()
  }

  def saveFetchQueue {
    val queueUsersFile = new File(fetchQueueFileName)
    val writer = new BufferedWriter(
      new FileWriter(queueUsersFile))
    fetchQueue.foreach { user => writer.write(user + "\n") }
    writer.close()
  }
}
