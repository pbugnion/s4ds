
# Mongo deserialization with type classes

This is a stub implementation of a wrapper around Casbah for de-serializing custom types from `DBObject` fields using type classes.

We start by defining an abstract `MongoReader[T]` trait with a `read` method that converts from `Any` (the compile-time type of fields in `DBObject` instances) to `T`. We then give specific implementations of `MongoReader[T]` for all the types we want to de-serialize (just `String` and the `Language` enum in this stub).

We then use the *Pimp my library* pattern to add a `.read[T]` method to Casbah's `DBObject` (in `Implicits.scala`). The `.read[T]` method relies on the existence of a `MongoReader[T]` type class for the type `T` we de-serialize to.

We can now de-serialize `DBObject` instances to custom fields. For instance:

    val obj = DBObject("name" -> "s4ds", "language" -> "Scala")

    val language = obj.read[Language.Value]("language")
    val name = obj.read[String]("name")

In this example, `language` has type `Language.Value` and `name` has type `String`.

Type classes provide an elegant way to extract custom Scala types from `DBObject` instances.
