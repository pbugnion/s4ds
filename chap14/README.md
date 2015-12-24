
# Chapter 14

This chapter builds a web application using the Play framework and D3.

`ghub-display` is a web API and application built using the Play framework that
provides the guiding thread for this chapter. To view a working version of the
application, go to `app.scala4datascience.com` with a web browser.

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

Navigate to `127.0.0.1:9000` in your browser to see the application.

## Running in production mode

To run the application in production, start by creating a jar containing the app and the dependencies by running:

    $ activator dist

This will create a zip archive called `ghub-display-1.0-SNAPSHOT.zip` with the application itself, its dependencies and a shell script to run it, in the directory `target/universal/`.

To run the application in production, just unzip the archive and run  the shell script `./ghub-display-1.0-SNAPSHOT/bin/ghub-display`. The application runs on port 9000 by default.

It is likely that you want the application to listen to port 80, the default port for HTTP, rather than on port 9000. To run it on port 80, you can specify the port explicitly when starting the application:

    $ sudo ./ghub-display-1.0-SNAPSHOT/bin/ghub-display -Dhttp.port=80

Since 80 is a privileged port, you will need to run this with root access. Alternatively, to avoid having to run your application as root (this is preferable), set up a traditional web server like Apache to forward requests to port 9000. To do this on a Ubuntu instance, start by installing Apache and `mod_proxy` using:

    sudo apt-get install -y apache2
    sudo apt-get install -y libapache2-mod-proxy-html libxml2-dev
    sudo a2enmod proxy proxy_ajp proxy_http rewrite deflate headers proxy_balancer proxy_connect proxy_html

You can then tell Apache to forward connections to port 9000 by inserting the following lines in `/etc/apache2/apache2.conf`:

    <VirtualHost *:80>
        ProxyPreserveHost On
        ServerName app.scala4datascience.com
        ProxyPass  /excluded !
        ProxyPass / http://127.0.0.1:9000/
        ProxyPassReverse / http://127.0.0.1:9000/
    </VirtualHost>

Restart the Apache server with:

    $ sudo service apache2 restart

The `vagrant` directory contains a Vagrant configuration for setting this up on a Vagrant box. The `bootstrap.sh` script transfers nearly verbatim to setting up a production environment on an EC2 instance.
