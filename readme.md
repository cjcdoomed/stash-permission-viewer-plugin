# The Permission Viewer Plugin

Extends your Atlassian Stash instance to display project and repository permissions to anyone with view access.

The plugin shows two tabs:

* A project permission tab that displays project admin, write and read permissions
* A repository permission tab which repository admin, write and read permissions as well as permissions inherited from the project level.

Groups with permissions are expanded out to show group name and user membership.

Once the plugin is installed an "View Permissions" tab will display in the project and repository tab panels.

You must install the Atlassian Plugin SDK - Instructions here:https://developer.atlassian.com/stash/docs/latest/how-tos/creating-a-stash-plugin.html

Atlassian Plugin SDK Command Reference: https://developer.atlassian.com/display/DOCS/Command+Reference

'atlas-run' will run the plugin within Stash
'atlas-debug' will run the plugin within Stash with a remote debug port
'atlas-package' will create the deployment jar to distribute
