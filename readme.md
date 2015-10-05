# The Permission Viewer Plugin

Extends your Atlassian Bitbucket instance to display project and repository permissions to anyone with view access.

The plugin shows two tabs:

* A project permission tab that displays project admin, write and read permissions
* A repository permission tab which repository admin, write and read permissions as well as permissions inherited from the project level.

Groups with permissions are expanded out to show group name and user membership.

## Installing the Plugin

[Download](https://marketplace.atlassian.com/plugins/com.orbitz.bitbucket.plugins.permission-viewer-plugin "Permission Viewer Plugin") the plugin from the Atlassian Marketplace.

The plugin can be installed via UPM by navigating to Administration > Plugins & Apps > Plugins. Go to "Install" tab and use "Upload Plugin" option to upload the plugin if it cannot be found on Atlassian Marketplace. Or use the install option if the plugin is found on Marketplace.
Once the plugin is installed an "View Permissions" tab will display in the project and repository tab panels.


## Modifying the plugin

Install the [Atlassian Plugin SDK](https://developer.atlassian.com/bitbucket/docs/latest/how-tos/creating-a-bitbucket-plugin.html "Creating a Bitbucket Plugin")

`atlas-run` will run the plugin within Bitbucket

`atlas-debug` will run the plugin within Bitbucket with a remote debug port

`atlas-package` will create the deployment jar to distribute
