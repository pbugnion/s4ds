
import com.mongodb.casbah.Imports._

object Implicits {

  implicit def pimpDBObject(o:DBObject):RichDBObject = new RichDBObject(o)

}
