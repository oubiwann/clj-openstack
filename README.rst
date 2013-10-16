########################################
Clojure Bindings for the Rackspace Cloud
########################################

The info in this README is outdated. More coming soon!

Requires [clojure-contrib](http://github.com/richhickey/clojure-contrib/tree/master) and
[clojure-http-client](http://github.com/technomancy/clojure-http-client/tree/master)

Here's a simple example:

.. code:: clojure

    (ns clojure.rackspace.example
      (:require [clojure.rackspace.cloudserver :as rs]))

    (let [session (rs/login "<username>" "<api key>")]
        (rs/list-servers session))
