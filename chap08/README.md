
# Chapter 8

## Code examples for chapter 8.

The `GitHubUserIterator.scala` source file contains a `GitHubUserIterator` class that iterates through users in the GitHub API. Construct it with:

    scala> val it = new GitHubUserIterator

or 

    scala> val it = new GitHubUserIterator("2502761dc...") // GitHub OAuth token

`it` is then just a normal Scala iterator:

    scala> it.next
    User = User(mojombo,1,List(Repo(30daysoflaptops.github.io,26899533,CSS), Re...

`User.scala` and `Repo.scala` are case classes representing a user and a repository.

The following classes are runnable examples:

 - `Credentials.scala` attempts to read environment variables MONGOPASSWORD and MONGOUSER from the environment variables, and connect to a MongoDB server using SCRAM-SHA1.
 - `InsertUsers.scala` fetches the first 500 users from GitHub, and their repositories and inserts them into a MongoDB database called `"github"`. Requires the classes `User`, `Repo` and `GitHubUserIterator` to be present in the same package.
 - `RepoNumber` prints the average number of repositories of everyone in the database. 
