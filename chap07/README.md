
# Chapter 7

 * `GitHubUser.scala`: Fetches basic information for a single user from the GitHub API. Takes the user name as a command line argument. Run from within SBT using, for instance `> runMain GitHubUser odersky`.
 * `GitHubUserConcurrent.scala` Fetches basic information for multiple users from the GitHub API, concurrently and in a fault-tolerant manner. Takes a list of user names as command line arguments. Run from within SBT, for instance `> runMain GitHubUserConcurrent odersky derekwyatt not-a-user-654`.
 * `GitHubUserAuth.scala` Fetches basic information for multiple users from the GitHub API, using OAuth to authenticate. For this to work, edit the `token` field in the method `fetchUserFromUrl`. Run from within SBT, for instance `> runMain GitHubUserAuth odersky derekwyatt not-a-user-654`.

The `build.sbt` declares a dependency on `json4s` and `scalaj-http`. Thus, opening an SBT console in this directory will open a Scala REPL with access to these libraries.
