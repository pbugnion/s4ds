import java.sql.Date

case class Transaction(
    id:Option[Int], // unique identifier
    candidate:String, // candidate receiving the donation
    contributor:String, // name of the contributor
    contributorState:String, // contributor state
    contributorOccupation:Option[String], // contributor job
    amount:Long, // amount in cents
    date:Date // date of the donation
)
