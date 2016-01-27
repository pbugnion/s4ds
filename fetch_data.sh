#!/bin/bash

echo "Fetching data for chapter 6"
echo

curl --compressed 'data.scala4datascience.com/fec/ohio.csv.gz' | gunzip > chap06/data/ohio.csv
curl --compressed 'data.scala4datascience.com/fec/us.csv.gz' | gunzip > chap06/data/us.csv

echo
echo "Fetching data for chapter 10"
echo

curl --compressed 'data.scala4datascience.com/ling-spam.tar.gz' | tar xzf - -C chap10

echo
echo "Fetching data for chapter 12"
echo

curl --compressed 'data.scala4datascience.com/ling-spam.tar.gz' | tar xzf - -C chap12
