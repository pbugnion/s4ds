
import java.sql._

trait SqlReader[T] {
  def read(results:ResultSet, field:String):T
}

object SqlReader {
  implicit object StringReader extends SqlReader[String] {
    def read(results:ResultSet, field:String):String =
      results.getString(field)
  }

  implicit object GenderReader extends SqlReader[Gender.Value] {
    def read(results:ResultSet, field:String):Gender.Value =
      Gender.withName(StringReader.read(results, field))
  }
}
