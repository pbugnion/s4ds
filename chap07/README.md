
# Chapter 7: Web APIs

This chapter teaches you how to query web APIs. We use the GitHub API as example.

For some of the examples in this chapter, you will need to pass an OAuth token to the GitHub API. To generate a token, you will need an account on GitHub. Once you have an account, open the profile menu (top right corner) and click on *Settings* > *Personal Access Tokens*. Enter default values for all the fields in the form. This will generate an OAuth token, which is just a long hexadecimal string. Copy the string and add it to your environment with:

    $ export GHTOKEN="e83638..."

This directory contains the following code files:

 * `GitHubUser.scala`: Fetches basic information for a single user from the GitHub API. Takes the user name as a command line argument. Run from within SBT using, for instance `> runMain GitHubUser odersky`.
 * `GitHubUserConcurrent.scala` Fetches basic information for multiple users from the GitHub API, concurrently and in a fault-tolerant manner. Takes a list of user names as command line arguments. Run from within SBT, for instance `> runMain GitHubUserConcurrent odersky derekwyatt not-a-user-654`.
 * `GitHubUserAuth.scala` Fetches basic information for multiple users from the GitHub API, using OAuth to authenticate. You will need to set the `GHTOKEN` environment variable for this to work (see above). Then, run from within SBT, for instance `> runMain GitHubUserAuth odersky derekwyatt not-a-user-654`.

The `build.sbt` declares a dependency on `json4s` and `scalaj-http`. Thus, opening an SBT console in this directory will open a Scala REPL with access to these libraries.
