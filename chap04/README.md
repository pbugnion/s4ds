
# Chapter 4: Parallel collections and futures

## Parallel collections

 - `LogisticRegression.scala`: an extension of the `LogisticRegression` class written in chapter 2 that also contains `predictProbabilitiesMany` and `classifyMany` methods for generating predictions on a test set.
 - `HWData.scala`: routines for loading the height-weight data from CSVs.
 - `RandomSubsample.scala`: class to perform random subsample cross-validation in parallel using a parallel range.
 - `RandomSubsampleDemo.scala`: Demonstration program showing how to use the `RandomSubsample` class.

## Futures

 - `BlockDemo.scala`: simple program demonstrating how to block to await the completion of a future.
 - `StockPriceDemo.scala`: Simple command line application for fetching stock prices from the [Markit on demand]() API. Stock prices are fetched asynchronously to avoid blocking the user interface.
