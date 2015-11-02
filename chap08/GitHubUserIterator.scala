
import scalaj.http._
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.collection.mutable


class GitHubUserIterator(private val token:Option[String]) extends Iterator[User] {

  def this() = this(None)

  private val userQueue = mutable.Queue.empty[JValue]
  private var nextUrl = "https://api.github.com/users"

  def hasNext = true

  def next:User = {
    if (userQueue.isEmpty) {
      populateQueue
    }
    val user = userQueue.dequeue
    val JString(repoUrl) = user \ "repos_url"
    val JString(login) = user \ "login"
    val JInt(id) = user \ "id"
    User(login = login, id = id.toLong, repos = fetchReposForUser(repoUrl))
  }

  private def populateQueue {
    val response = withAuthentication(Http(nextUrl)).asString
    val body = response.body
    val linkHeader = response.headers.getOrElse("Link", 
      throw new IllegalArgumentException("No link header found in reply.")
    )
    nextUrl = parseLinkHeader(linkHeader)
    val json = parse(body)
    val JArray(userDocuments) = json
    userDocuments.foreach { doc => userQueue.enqueue(doc) }
  }

  private def parseLinkHeader(header:String):String = {
    val nextUrl = for {
      headerFragment <- header.split(",")
      if (headerFragment.contains("rel=\"next\""))
      wrappedUrl = headerFragment.split(";").head
      url = wrappedUrl.replace("<","").replace(">","")
    } yield url
    nextUrl.head
  }

  private def fetchReposForUser(reposUrl:String):List[Repo] = {
    val request = withAuthentication(Http(reposUrl)).asString
    val json = parse(request.body)
    val JArray(repoDocuments) = json
    val repos = for {
      JObject(document) <- repoDocuments
      JField("name", JString(name)) <- document
      JField("id", JInt(id)) <- document
      JField("language", JString(language)) <- document
    } yield Repo(name, id.toLong, language)
    repos.toList
  }

  private def withAuthentication(request:HttpRequest):HttpRequest = {
    token match {
      case None => request
      case Some(t) => request.header("Authorization", "token "+t)
    }
  }
}


