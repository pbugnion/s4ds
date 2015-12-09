
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import com.github.tototoshi.csv._

object FECData {

  val DataDirectory = "./data/"

  private val dateParser = new SimpleDateFormat("DD-MMM-YY")

  private def load(fileName:String):FECData = {
    val reader = CSVReader.open(new File(DataDirectory + fileName))
    val transactions = for { 
      row <- reader.iteratorWithHeaders
      id = None
      candidate = row("candidate")
      contributor = row("contributor_name")
      state = row("contributor_state")
      occupation = row("contributor_occupation") match {
        case "" => None
        case v => Some(v)
      }
      amount = (row("amount").toDouble*100).toInt
      date = new Date(dateParser.parse(row("date")).getTime)
    } yield Transaction(id, candidate, contributor, state, occupation, amount, date)
    
    new FECData(transactions)
  }

  def loadAll:FECData = load("us.csv")

  def loadOhio:FECData = load("ohio.csv")


}

class FECData(val transactions:Iterator[Transaction])


