
# Chapter 12: Distributed machine learning with MLlib

This chapter introduces the MLlib machine learning library, which runs on top of Spark. MLlib is a toolkit for building distributed machine learning pipelines.

You will need a working Spark installation for this chapter. Follow the installation instructions in the book, or on the Spark website.

We use the same ling-spam dataset as in chapter 10.

To run the examples in this chapter:

 1. Download the raw data as a [tar.gz archive](http://data.scala4datascience.com/ling-spam.tar.gz) or as a [zip archive](http://data.scala4datascience.com/ling-spam.zip), depending on whether you would rather unpack a tar archive or a zip file.
 2. Unpack the data in the same directory as the sample code (for instance, if you downloaded the `tar.gz` file):

        $ tar xzf ling-spam.tar.gz

    or (for the zip file):

        $ unzip ling-spam.tar.gz

    This should create a `spam/` and a `ham/` directory. Unpacking the archive may take a minute or so.

 3. Build the code using SBT assembly:

        $ sbt assembly

    This creates a JAR file in the `target/scala-2.10/` directory

 4. Run the code through `spark-submit`:

        $ spark-submit --class LogisticRegressionDemo target/scala-2.10/spam_filter-assembly-0.1-SNAPSHOT.jar
        $ spark-submit --class ROC target/scala-2.10/spam_filter-assembly-0.1-SNAPSHOT.jar

This chapter contains two example files:

 - `LogisticRegressionDemo.scala` trains a logistic regression model to determine whether an email is spam or ham. It saves a DataFrame with the training set and a DataFrame with the test set (including predictions) to Parquet files.
 - `ROC.scala` takes the DataFrames stored in the Parquet files and plots and ROC curve.
