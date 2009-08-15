# Clojure Rackspace Cloud Server

by Samuel Hughes

Requires [clojure-contrib](http://github.com/richhickey/clojure-contrib/tree/master) and
[clojure-http-client](http://github.com/technomancy/clojure-http-client/tree/master)

Here's a simple example:

    (ns clojure.rackspace.example
      (:require [clojure.rackspace.cloudserver :as rs]))
      
    (def session (rs/login "<username>" "<api key>"))

    (rs/list-servers session)

