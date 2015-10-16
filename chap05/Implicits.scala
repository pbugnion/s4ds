// Implicits.scala
import java.sql._

// Implicit conversion methods are often put in 
// an object called Implicits.
object Implicits {
  implicit def pimpConnection(conn:Connection) = 
    new RichConnection(conn)
  implicit def depimpConnection(conn:RichConnection) =  
    conn.underlying

  implicit def pimpResultSet(results:ResultSet) =
    new RichResultSet(results)
}
