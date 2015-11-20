
import scala.io._
import org.json4s._
import org.json4s.native.JsonMethods._
import java.io._

object FetchData extends App {

  val ghubResponse = Source.fromURL("https://api.github.com/users/odersky/repos").mkString
  val jsonResponse = parse(ghubResponse)
  val JArray(repos) = jsonResponse

  val writer = new PrintWriter(new File("odersky_repos.json"))

  repos.foreach {
    r => writer.println(compact(render(r)))
  }

  writer.close

}

