# Eclipse Minishift Support

This is an Eclipse IDE plugins which allows you to use the variable `${minishift_ip}` in
any location that Eclipse allows you to use variables and have the current minishift IP inserted.

It is mainly used for inserting the current IP of the Minishift instance into a launch configuration.

## How to install

[![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3672636 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client")

You can also add the following URL as a software source into your Eclipse preferences and install
the plugin manually: https://ctron.github.io/eclipse-minishift

## Usage

The plugin extends the Eclipse variable lookup system which maybe used in different locations
(watch out for the button "Variables…" next to an entry field).

### Getting the Minishift IP

In order to get the IP of the minishift instance use:

    ${minishift_ip}

Which will get replaced with the IP address of the minishift instance: e.g.: `172.10.5.1`

If minishift is not running then the lookup will fail.

### Getting a service URL

To get the URL of a service use:

    ${minishift_service:http://service-name}

This will retrieve the service's URL: e.g. `http://hono-adapter-rest-vertx-hono.192.168.42.43.nip.io`

It is also possible to retrieve the HTTPS based URL:

    ${minishift_service:https://service-name}

The service name must be prefixed with `http://` or `https://`.

If minishift is not running or the service is unknown then the lookup will fail.