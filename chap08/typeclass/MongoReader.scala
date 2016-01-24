
trait MongoReader[T] {
  def read(v:Any):T
}

object MongoReader {
  implicit object StringReader extends MongoReader[String] {
    def read(v:Any):String = v match {
      case s:String => s
    }
  }
  
  implicit object LanguageReader extends MongoReader[Language.Value] {
    def read(v:Any):Language.Value = v match {
      case s:String => Language.withName(s)
    }
  }
}
