
import scala.io._
import org.json4s._
import org.json4s.native.JsonMethods._

object GitHubUser {

  implicit val formats = DefaultFormats

  case class User(
    val id:Long,
    val username:String
  )

  def fetchUserFromUrl(url:String):User = {
    val response = Source.fromURL(url).mkString
    val jsonResponse = parse(response)
    extractUser(jsonResponse)
  }

  def extractUser(obj:JValue):User = {
    val o = obj.transformField {
      case ("login", name) => ("username", name)
    }
    o.extract[User]
  }

  def main(args:Array[String]) {
    val name = args(0)
    val user = fetchUserFromUrl(s"https://api.github.com/users/$name")

    println(s" ** Extracted for $name:")
    println()
    println(user)

  }

}
