#  Scala for data science examples

This repository contains the code examples for [Scala for data science](http://pascalbugnion.net/book.html). The aim of the book is to teach people who know a bit of Scala about useful libraries and tools for writing data science applications.

The examples are divided as follows:

 - Chapter 2 teaches you how to use [Breeze](https://github.com/dlwh/breeze) for linear algebra and function optimization. The example programs show you how to write a basic logistic regression model.
 - Chapter 3 introduces [breeze-viz](https://github.com/scalanlp/breeze/wiki/Quickstart#breeze-viz) for drawing basic two-dimensional plots.
 - Chapter 4 teaches you how to parallelize cross-validation pipelines using parallel collections, and builds a small command line utility for querying a web API in parallel using futures.
 - Chapter 5 introduces how to interact with SQL databases from Scala. We write wrappers around JDBC to allow interacting with it more functionally. We introduce several design patterns commonly used in Scala, including implicit conversions to extend existing libraries and typeclasses.
 - Chapter 6 introduces [Slick](http://slick.typesafe.com), a wrapper around JDBC that maps SQL tables directly to case classes.
 - Chapter 7 teaches you how to interact with web APIs and how to convert JSON objects to Scala classes.
 - Chapter 8 introduces [Casbah](https://mongodb.github.io/casbah/), a library for interacting with [MongoDB](https://www.mongodb.org), a leading NoSQL database.
 - Chapter 9 introduces the [Akka](http://akka.io) framework for building concurrent applications. We build a web crawler that crawls the graph of followers on GitHub.
 - Chapters 10 and 11 introduce [Apache Spark](http://spark.apache.org), a framework for processing batches of data over several different computers.
 - Chapter 12 walks through the construction of a spam filter in MLlib, a machine learning library for training distributed algorithms on large datasets in memory.
 - Chapters 13 and 14 introduce the [Play framework](https://www.playframework.com) for building web applications. In chapter 13, we build a REST API to deliver information about a user's repositories on GitHub, and in chapter 14, we build on this API to build a single-page web application with [D3](d3js.org) graphs.

To run the examples, start by installing [SBT](http://www.scala-sbt.org/release/tutorial/Setup.html), the (main) build tool for Scala projects.

Once you have installed SBT, download this repository using:

    $ git clone https://github.com/pbugnion/s4ds.git

You can then navigate to each chapter and type `sbt run` in a terminal to run the source code, or `sbt console` to open a Scala console with all the dependencies for that particular chapter. Read the README for each chapter for more detail.
