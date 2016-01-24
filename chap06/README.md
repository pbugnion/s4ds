
# Chapter 6: Slick -- an interface to SQL

For the code examples in this chapter, you will need a MySQL database running on port 3306.

We use FEC filings for candidates running for president of the US in 2012 as dataset for this chapter. You can use one of two datasets with the code examples:

 - All the filings for Ohio. Use this if your computer is not particularly fast or if you want more responsive behavior. This is available as a [tar archive](data.scala4datascience.com/ohio.csv.gz) or as a [zip archive](data.scala4datascience.com/ohio.csv.zip).
 - The filings for the entire US. Use this if you want to grapple with a larger dataset. This is available as a [tar archive](data.scala4datascience.com/us.csv.gz) or as a [zip archive](data.scala4datascience.com/us.csv.zip).

Once you have downloaded the data files, unpack them into the `data/` directory.

The code files are:

 - `FECData.scala`: exposes an iterator interface to the FEC data. Use as:

       val ohioIterator = FECData.loadOhio

 or

       val usIterator = FECData.loadAll

 - `Transaction.scala`: case class representing an individual transaction.
 - `Tables.scala`: the Slick interface to the `transactions` table in the SQL database. Converts between SQL and instances of the `Transaction` class.
 - `SlickDemo.scala`: sample routines for inserting and querying the FEC data using Slick.
