
# Chapter 5: Scala and SQL through JDBC

The examples in this chapter demonstrate how one might build a functional interface to access JDBC.

To run the examples for this chapter, you will need a MySQL database server running on port 3306.

## Model classes

 - `Gender.scala`: a Scala enum to represent gender.
 - `Physicist.scala`: case class describing a physicist.

## Loan pattern and 'pimp my library' examples

 - `SqlUtils.scala`: Routines for opening a connection using the loan pattern and for wrapping a `ResultSet` instance in a Scala stream.
 - `RichConnection.scala`: class wrapping a JDBC connection to allow opening `ResultSet` instances using the loan pattern. This class should not be used explicitly. Instead, we provide implicit conversions from a JDBC `Connection` instance to an instance of `RichConnection` using the *pimp my library* pattern. These implicit conversions are provided in the `Implicits.scala` code file.

## Type classes

 - `SqlReader.scala`: abstract trait and specific implementations of a `SqlReader[T]` trait that exposes a `read` method describing how to extract an instance of type `T` from a field in a `ResultSet`.
 - `RichResultSet.scala`: class wrapping a JDBC `ResultSet` instance to offer a `read[T]` method that extracts an instance of `T` from a `ResultSet`. The `read[T]` method relies on the existence of a `SqlReader[T]` type class. The `RichResultSet` class should not be used explicitly. Rather, we define a conversion in `Implicits.scala` that implicitly adds the functionality in the `RichResultSet` class to a JDBC `ResultSet`.
 - `Implicits.scala`: Implicit conversions from a JDBC `Connection` to a `RichConnection` and back, and from a `ResultSet` to a `RichResultSet`.

## Data access object

Data access objects (DAOs) are objects that provide functions to serialize and deserialize specific classes to and from a SQL database. They assume the SQL table follows a specific schema.

 - `PhysicistDao.scala` provides a method for reading every row in a `physicists` SQL table, converting each row to an instance of the `Physicist` case class.
 - `PhysicistDaoDemo.scala` exercises the `PhysicistDao` class.
