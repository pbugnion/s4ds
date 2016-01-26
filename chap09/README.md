
# Chapter 9: Concurrency with Akka

This chapter explores the the Akka actor library for writing concurrent application. The code examples focus on building a concurrent web crawler to fetch the graph of followers on GitHub: a graph where nodes correspond to GitHub users and directional edges indicate who is followed by whom.

To run any of the examples in this chapter, you will need to authenticate with the GitHub API. Generate a GitHub token (follow the instructions in the book, or just go to your profile on GitHub and click Settings > Personal Access tokens). Copy the long hexadecimal string and inject it into your environment using:

    $ export GHTOKEN="abc123..."

You may want to add this line to your `.bashrc` file to avoid having to re-type it.

Each folder is a standalone application. The folders correspond to:

 - `single_threaded`: a single-threaded version of the application. This does not use Akka at all; its purpose is to demonstrate how one might build the crawler in a single-threaded manner.
 - `hello_akka` builds a simple actor that just echoes the message it receives.
 - `hello_akka_case_classes` builds a simple actor that uses case classes to represent its messages.
 - `fetchers_alone` is a very simple program capable of querying the GitHub API for specific users, using several fetcher actors in parallel. The response is just printed to screen.
 - `fetchers_routing` is similar, but uses automatic routing to balance queries between the fetchers.
 - `all_workers` adds an actor to interpret the GitHub response and extract arrays of followers.
 - `ghub_crawler` adds functionality for managing a queue of users to be fetched. This is the first true crawler: any follower extracted from the response is automatically added to the queue of users to be fetched. The crawler would therefore crawl the entire follower graph, if it weren't for the API rate limits.
 - `ghub_crawler_fault_tolerant` adds lifecycle hooks that automatically write the state of the queue to a file when the queue manager dies.
