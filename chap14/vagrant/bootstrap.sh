#!/usr/bin/env bash

sudo apt-get update
sudo apt-get upgrade -y

sudo apt-get install -y unzip

echo "Installing Java 8"
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get -y -q update

echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get -y -q install oracle-java8-installer
sudo update-java-alternatives -s java-8-oracle

echo "Installing Apache server"

sudo apt-get install -y apache2
sudo apt-get install -y libapache2-mod-proxy-html libxml2-dev
sudo a2enmod proxy proxy_ajp proxy_http rewrite deflate headers proxy_balancer proxy_connect proxy_html

sudo cp /vagrant/apache2.conf /etc/apache2/
sudo service apache2 restart
