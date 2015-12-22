
# Vagrant deployment

Defines a Vagrant box for testing Play framework deployments. The box includes:

 - Ubuntu 14.04.
 - Java 8
 - Apache server set to forward requests to port 80 (the default HTTP port) to port 9000, the default port used by the Play framework.


 Note that the box downloads Java 8 as provided by Oracle. By opening this box, you are implicitly agreeing to the associated [terms and conditions](http://www.oracle.com/technetwork/java/javase/jdk-8-readme-2095712.html), as set by Oracle.

 The box forwards its port 9000 to the host machine's port 9000, and its port 80 to the host's port 8080.

 To run `ghub-display` on the box:

1. copy the four files in this directory to a new directory (outside of source control) called, for instance, `ghub_deployment`.
2. Copy the zip file produced by running `activator dist` to `ghub_deployment`.
3. Run `vagrant up` in `ghub_deployment` to create the box. This will take a while the first time you run it as it needs to download the Ubuntu image that forms the base of the box.
4. Run `vagrant ssh` to ssh into the box. The `/vagrant` folder of the VM should contain the zip file containing the app, as well as a `run.sh` shell script. Copy this shell script to your home directory on the box and run it. This should unzip the application and run it on port 80. To verify that this works, you can poll the API from the VM using `wget`:

        $ wget "http://127.0.0.1/api/repos/odersky"

If this works, your application is available on port 80. To see it in action, point your browser (on the host machine, not the VM) to `127.0.0.1:8080`. You should see the application running. Note that you need to point your web browser to port 8080, rather than 80, since the Vagrant box forwards its port 80 to the host's port 8080 (port 80 is a privileged port, so only applications run by root can bind to it).

The `bootstrap.sh` script does most of the deployment work. You can re-use it almost verbatim to bootstrap, for instance, an EC2 instance running Ubuntu 14.04. Just modify the line that reads `sudo cp /vagrant/apache2.conf /etc/apache2` so that it places the `apache2.conf` file included in this directory in the directory `/etc/apache2`.
