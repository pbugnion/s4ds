
import com.mongodb.casbah.Imports._

object Credentials extends App {

  val username = sys.env.getOrElse("MONGOUSER",
    throw new IllegalStateException(
      "Need a MONGOUSER variable in the environment")
  )
  val password = sys.env.getOrElse("MONGOPASSWORD",
    throw new IllegalStateException(
      "Need a MONGOPASSWORD variable in the environment")
  )

  val host = "127.0.0.1"
  val port = 27017

  val uri = s"mongodb://$username:$password@$host:$port/?authMechanism=SCRAM-SHA-1"

  val client = MongoClient(MongoClientURI(uri))
}
