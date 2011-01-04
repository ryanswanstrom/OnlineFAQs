An application built with the play framework.  It can be hosted on Google
App Engine.

Prerequites
====
The [Google App Engine SDK for Java](http://code.google.com/appengine/downloads.html#Google_App_Engine_SDK_for_Java) is needed.
Both the siena and gae modules are required for the project.  If the
modules have not been previously installed, type the following at the
command-line.

1. play install gae
1. play install siena
1. cd $PLAY_HOME/modules
1. ln -s siena-1.3/ siena
1. ln -s gae-1.4/ gae



Getting Started
====

* git clone git@github.com:swGooF/OnlineFAQs.git
* cd OnlineFaqs
* play run
* Then test it out locally at http://localhost:9000
* Ctrl-C
* play gae:deploy --gae=<location to GAE SDK>

