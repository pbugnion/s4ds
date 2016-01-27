
# Chapter 13: Web APIs with Play

This chapter explores the Play framework. We build a web API, `ghub-display`, that provides
a JSON array describing a user's repositories.

## Prerequisites

The Play framework requires the Java 8 JDK. To verify which version of Java you are running, enter the following in a terminal:

    $ java -version

If the first line reads "java version 1.8.x_xx", you are good to go. Otherwise, you will need to install Java 8. Refer to the preface for instructions.

## Installation

To run any of the code in this chapter, you need to install Typesafe Activator.

On a Mac, you can do this via Homebrew:

    $ brew install typesafe-activator

On Linux or Windows, you need to download a zip archive from:

    https://www.typesafe.com/activator/download

This downloads a zip archive. Unzip the archive with:

    $ unzip typesafe-activator-*.zip

This will create a directory called `activator-dist-1.3.7` (the version number may differ), inside of which is a shell script called `activator` or `activator.bat`. Add these scripts to your shell path by, eg. on Linux, adding the following to your `.bashrc`:

    PATH=$PATH:/path/to/activator-dist-1.3.7/

## Running in development mode

To run the application, cd to the `ghub-display` directory and type:

    $ activator run

To test the API, enter the following address in your web browser:

    127.0.0.1:9000/api/repos/odersky

This should print a list of all the Github repositories owned by Martin Odersky. You can replace `odersky` with any other GitHub login name.

## Running in production mode

See the README file for chapter 14 for instructions on how to deploy the API.
