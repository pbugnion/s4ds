
import com.mongodb.casbah.Imports._

class RichDBObject(val underlying:DBObject) {

  def read[T:MongoReader](field:String) =
    implicitly[MongoReader[T]].read(underlying(field))

}
