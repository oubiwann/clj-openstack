#############
clj-rackspace
#############

*Clojure Bindings for the Rackspace Cloud*

Background
==========

TBD

Links
-----

 * http://docs.rackspace.com/ - documentation for Rackspace Cloud
   * http://docs.rackspace.com/servers/api/v2/cs-devguide/content/ch_preface.html - Cloud Servers docs
 * http://www.rackspace.com/cloud/ - information about Rackspace Cloud services
 * https://mycloud.rackspace.com/ - sign in to the Rackspace Cloud (OpenStack)

Installation
============

TBD

Usage
=====

The info in this README is outdated. More coming soon!

Here's a simple example:

.. code:: clojure

    (ns clojure.rackspace.example
      (:require [clojure.rackspace.cloudserver :as rs]))

    (let [session (rs/login "<username>" "<api key>")]
        (rs/list-servers session))
