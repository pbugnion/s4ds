import java.sql._

object SqlUtils {

  /** Create an auto-closing connection using 
    * the loan pattern */
  def usingConnection[T](
    db:String,
    host:String="127.0.0.1",
    user:String="root",
    password:String="",
    port:Int=3306
  )(f:Connection => T):T = {
    
    // Create the connection
    val Url = s"jdbc:mysql://$host:$port/$db"
    Class.forName("com.mysql.jdbc.Driver")
    val connection = DriverManager.getConnection(
      Url, user, password)

    // give the connection to the client, through the callable 
    // `f` passed in as argument
    try {
      f(connection)
    }
    finally {
      // When client is done, close the connection
      connection.close()
    }
  }

  def stream(results:ResultSet):Stream[ResultSet] = 
    if (results.next) { results #:: stream(results) }
    else { Stream.empty[ResultSet] }
}
