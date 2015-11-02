
import com.mongodb.casbah.Imports._

object InsertUsers {

  lazy val token:Option[String] = sys.env.get("GHTOKEN") orElse {
    println("No token found: continuing without authentication")
    None
  }

  def repoToDBObject(repo:Repo):DBObject = DBObject(
    "github_id" -> repo.id,
    "name" -> repo.name,
    "language" -> repo.language
  )

  def userToDBObject(user:User):DBObject = DBObject(
    "github_id" -> user.id,
    "login" -> user.login,
    "repos" -> user.repos.map(repoToDBObject)
  )

  def insertUsers(coll:MongoCollection)(users:Iterable[User]) {
    users.foreach { user => coll += userToDBObject(user) }
  }

  def ingestUsers(nusers:Int)(inserter:Iterable[User] => Unit) {
    val batchSize = 100
    val it = new GitHubUserIterator(token)
    print("Inserted #users: ")
    it.take(nusers).grouped(batchSize).zipWithIndex.foreach {
      case (users, batchNumber) =>
        print(s"${batchNumber*batchSize} ")
        inserter(users)
    }
    println()
  }

  def main(args:Array[String]) {
    val coll = MongoClient()("github")("users")
    val nusers = 500
    coll.dropCollection()
    val inserter = insertUsers(coll)_
    ingestUsers(nusers)(inserter)
  }

}
