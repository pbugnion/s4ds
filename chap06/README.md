
# Chapter 6: Slick -- an interface to SQL

This chapter introduces Slick, a library that provides a functional interface to SQL databases.

For the code examples in this chapter, you will need a MySQL database running on port 3306.

We use FEC filings for candidates running for president of the US in 2012 as dataset for this chapter. You will need to download the code samples to run the examples in this chapter. You can use one of two datasets:

 - All the filings for Ohio. Use this if your computer is not particularly fast or if you want more responsive behavior. This is available as a [gzip archive](data.scala4datascience.com/ohio.csv.gz) or as a [zip archive](data.scala4datascience.com/ohio.csv.zip).
 - The filings for the entire US. Use this if you want to grapple with a larger dataset. This is available as a [gzip archive](data.scala4datascience.com/us.csv.gz) or as a [zip archive](data.scala4datascience.com/us.csv.zip).

Once you have downloaded the data files, unpack them into the `data/` directory.

The `FECData.scala` file exposes two iterators for iterating over the rows in the dataset. Use these as:

    val ohioIterator = FECData.loadOhio // every transaction in `ohio.csv`
    val usIterator = FECData.loadAll // every transaction in `us.csv`

The code examples are:
 - `Transaction.scala`: case class representing an individual transaction.
 - `Tables.scala`: the Slick interface to the `transactions` table in the SQL database. Converts between SQL and instances of the `Transaction` class.
 - `SlickDemo.scala`: sample routines for inserting and querying the FEC data using Slick.
