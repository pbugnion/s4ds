
# Chapter 8: Scala and MongoDB

This chapter introduces Casbah for querying MongoDB. We will query the GitHub API to obtain sample data to insert into a MongoDB database.

To run the code examples in this chapter, you will need a MongoDB server running locally on port 27017 (the default port for MongoDB).

The `GitHubUserIterator.scala` source file contains a `GitHubUserIterator` class that iterates through users in the GitHub API. Construct it with:

    scala> val it = new GitHubUserIterator

or

    scala> val it = new GitHubUserIterator("2502761dc...") // GitHub OAuth token

`it` is then just a normal Scala iterator:

    scala> it.next
    User = User(mojombo,1,List(Repo(30daysoflaptops.github.io,26899533,CSS), Re...

`User.scala` and `Repo.scala` contain case classes representing a user and a repository, respectively.

The following classes are runnable examples:

 - `Credentials.scala` attempts to read environment variables MONGOPASSWORD and MONGOUSER, and connect to a MongoDB server using SCRAM-SHA1 authentication.
 - `InsertUsers.scala` fetches the first 500 users and their repositories from GitHub and inserts them into a MongoDB database called `"github"`. Requires the classes `User`, `Repo` and `GitHubUserIterator` to be present in the same package.
 - `RepoNumber` prints the average number of repositories of everyone in the database.

The `typeclass` directory provides a stub implementation of a data-access layer through which to access a MongoDB database. 
