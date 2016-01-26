# Chapter 10: Distributed batch processing with Spark

This chapter explores how to use Apache Spark for batch distributed in memory processing of large datasets. The example program, `MutualInformation`, calculates the 'informativeness' of
words in spam classification. We use the *ling-spam* dataset as example dataset. This is a set of emails sent to a linguistics mailing list. Some of the emails are spam and some are legitimate.

You will need a working Spark installation for this chapter. Follow the installation instructions in the book, or on the Spark website.

To run the example:
 1. Download the raw data as a [tar.gz archive](http://data.scala4datascience.com/ling-spam.tar.gz) or as a [zip archive](http://data.scala4datascience.com/ling-spam.zip), depending on whether you would rather unpack a tar archive or a zip file.
 2. Unpack the data in the same directory as the sample code (for instance, if you downloaded the `tar.gz` file):

        $ tar xzf ling-spam.tar.gz

    or (for the zip file):

        $ unzip ling-spam.tar.gz

    This should create a `spam/` and a `ham/` directory. Unpacking the archive may take a minute or so.

 3. Build the code using SBT:

        $ sbt package

    This creates a JAR file in the `target/scala-2.10/` directory

 4. Run the code through `spark-submit`:

        $ spark-submit target/scala-2.10/spam_mi_2.10-0.1-SNAPSHOT.jar
