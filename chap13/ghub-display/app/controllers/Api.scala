package controllers

import play.api._
import play.api.mvc._
import play.api.libs.ws.WS
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import models.Repo

class Api extends Controller {

  implicit val writesRepo = new Writes[Repo] {
    def writes(repo:Repo) = Json.obj(
      "name" -> repo.name,
      "language" -> repo.language,
      "is_fork" -> repo.isFork,
      "size" -> repo.size
    )
  }

  implicit val readsRepoFromGithub:Reads[Repo] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "language").read[String] and
    (JsPath \ "fork").read[Boolean] and
    (JsPath \ "size").read[Long]
  )(Repo.apply _)


  def repos(login:String) = Action.async {
    val url = s"https://api.github.com/users/$login/repos"
    val request = WS.url(url).withHeaders("Content-Type" -> "application/json").get()
    request.map { r => 
      if (r.status == 200) {
        val reposOpt = Json.parse(r.body).validate[List[Repo]]
        reposOpt match {
          case JsSuccess(repos, _) => Ok(Json.toJson(repos))
          case _ => InternalServerError
        }
      }
      else {
        NotFound
      }
    }
  }

}
