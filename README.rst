#############
clj-rackspace
#############

*Pure Clojure Bindings for the Rackspace Cloud*


.. contents:: Table of Contents


Purpose
=======

Fun.

This project doesn't really aim to be anything special. As we need some
functionality, we'll add it. Initial focus will be on supporting only a subset
of Rackspace Cloud Services (version 2 Cloud Servers).


Installation
============

`clj-rackspace` is up on `Clojars`_. You can add it to your `project.clj` for
automatic download with the following:

.. code:: clojure

    (defproject your-project "1.2.3"
      ...
      :dependencies [[org.clojure/clojure "1.5.1"]
                      ...
                      [clj-rackspace "0.1.1"]]
      ...)

You can then use it in your project like so:

.. code:: clojure

    (ns your-project.client
      (:require [rackspace.api :as rax-api]))

Or from the REPL:

.. code:: clojure

    (require '[rackspace.api :as rax-api])

.. Links
.. -----
.. _Clojars: https://clojars.org/clj-rackspace


Running Example Code
====================

The easiest way to get started with `clj-rackspace` is to play with it in the
REPL. You'll need to download the code for this:

.. code:: bash

    $ git clone https://github.com/oubiwann/clj-rackspace
    $ cd clj-rackspace

Now just do this at the command line prompt:

.. code:: bash

    $ make shell

Which will dump you in the `rackspace.api` namespace:

.. code:: clojure

    nREPL server started on port 62531 on host 127.0.0.1
    REPL-y 0.2.1
    Clojure 1.5.1
        Docs: (doc function-name-here)
              (find-doc "part-of-name-here")
      Source: (source function-name-here)
     Javadoc: (javadoc java-object-or-class-here)
        Exit: Control+D or (exit) or (quit)
     Results: Stored in vars *1, *2, *3, an exception in *e

    rackspace.api=>

For the examples below, you will need to provide your own username, password,
and any data returned from Rackspace Cloud services.

To make things easier to read in the examples below, let's set up
pretty-printing:

.. code:: clojure

    rackspace.api=> (require '[clojure.pprint :refer [pprint]])
    nil
    rackspace.api=>


CloudServers Usage
==================


Logging In
----------

Via password:

.. code:: clojure

    rackspace.api=> (def response (login "alice" :password "z0mg11!!secret1!1"))
    #'rackspace.api/response
    rackspace.api=>

Via API key:

.. code:: clojure

    rackspace.api=> (login "alice" :apikey "0a12b33c444d5555ee0123456789ffff")
    {:orig-content-encoding "gzip" ... }
    rackspace.api=>

You will need to pass one of the two, however:

.. code:: clojure

    rackspace.api=> (login "alice")

    ExceptionInfo AuthError: Missing named parameter  ...
    rackspace.api=>


Working with Login Data
-----------------------

With our response data saved, we can now perform several operations with `auth`
utility functions.

Getting the token:

.. code:: clojure

    rackspace.api=> (pprint (get-token response))
    {:id "482664e7cf97408e82f512fad93abc98",
     :expires "2013-10-17T20:11:40.557-05:00",
     :tenant {:id "007007", :name "007007"},
     :RAX-AUTH:authenticatedBy ["PASSWORD"]}
    nil
    rackspace.api=>

Listing the regions:

.. code:: clojure

    rackspace.api=> (list-cloud-servers-regions response)
    (:syd :dfw :ord :iad)
    rackspace.api=>

Getting all the endpoints:

.. code:: clojure

    rackspace.api=> (pprint (get-cloud-servers-endpoints response))
    [{:region "SYD",
      :tenantId "007007",
      :publicURL "https://syd.servers.api.rackspacecloud.com/v2/007007",
      :versionInfo "https://syd.servers.api.rackspacecloud.com/v2",
      :versionList "https://syd.servers.api.rackspacecloud.com/",
      :versionId "2"}
     {:region "DFW",
      :tenantId "007007",
      :publicURL "https://dfw.servers.api.rackspacecloud.com/v2/007007",
      :versionInfo "https://dfw.servers.api.rackspacecloud.com/v2",
      :versionList "https://dfw.servers.api.rackspacecloud.com/",
      :versionId "2"}
     {:region "ORD",
      :tenantId "007007",
      :publicURL "https://ord.servers.api.rackspacecloud.com/v2/007007",
      :versionInfo "https://ord.servers.api.rackspacecloud.com/v2",
      :versionList "https://ord.servers.api.rackspacecloud.com/",
      :versionId "2"}
     {:region "IAD",
      :tenantId "007007",
      :publicURL "https://iad.servers.api.rackspacecloud.com/v2/007007",
      :versionInfo "https://iad.servers.api.rackspacecloud.com/v2",
      :versionList "https://iad.servers.api.rackspacecloud.com/",
      :versionId "2"}]
    nil
    rackspace.api=>

Optionally, you may provide a version number (version 2 is assumed by default):

.. code:: clojure

    rackspace.api=> (pprint (get-cloud-servers-endpoints response :version 1))
    [{:tenantId "007007",
      :publicURL "https://servers.api.rackspacecloud.com/v1.0/007007",
      :versionInfo "https://servers.api.rackspacecloud.com/v1.0",
      :versionList "https://servers.api.rackspacecloud.com/",
      :versionId "1.0"}]
    nil
    rackspace.api=>

If you know the region you want, you can get the URL for it simply with this:

.. code:: clojure

    rackspace.api=> (get-cloud-servers-region-url response :dfw)
    "https://dfw.servers.api.rackspacecloud.com/v2/007007"
    rackspace.api=>


Background
==========

This project was started in 2009 by Samuel Hughes. After a first implementation
against the v1 API of Rackspace Cloud Servers, the project went into
hibernation.

Clojure is a delight to code in, and although it supports fabulous
Java interop, using jclouds from Clojure is a bit tedious. One alternative
would be to take the `Clojure-idiomatic example`_ and grow that as a wrapper
around jclouds. That was seriously considered. Another alterative was to take
Samuel Hughes' work based on `technomancy`_'s `clojure-http-client`_ and develop
a pure Clojure implementation.

That just sounded like more fun :-) So here we are.

.. Links
.. -----
.. _Clojure-idiomatic example: https://github.com/jclouds/jclouds-examples/tree/master/compute-clojure
.. _technomancy: https://github.com/technomancy
.. _clojure-http-client: https://github.com/technomancy/clojure-http-client


Motivation
----------

There are two efforts driving this work:

#. Rackspace Cloud Servers users who write in Clojure want to have a library
   that can use quickly, easily, and in a Clojure-idiomatic way.

#. `storm-deploy`_ users would like to be able to deploy to Rackspace Cloud
   Servers.

A note about that last point: `pallet`_ (used by storm-deploy) currently uses
`jclouds`_, so it might simply be easier to use jclouds. Only time will tell. In
the meantime, we'll have fun playing with a pure-Clojure implementation.

.. Links
.. -----
.. _storm-deploy: https://github.com/nathanmarz/storm-deploy
.. _pallet: https://github.com/pallet/pallet
.. _jclouds: https://github.com/jclouds/jclouds


Dependencies
------------

In the four years since this project was started, HTTP clients in Clojure have
come a long way. We will be migrating away from the original implementation's
choice of clojure-http-client.

The two commonly recommended clients are:

* `clj-http`_ for synchronous/blocking client calls; this library is a Clojure
  wrapper for the Apache HTTP client library.

* `http.async.client`_ for asynchronous usage; it's based on the
  Asynchronous Http Client for Java.

We have started with the synchronous client. Hopefully, we'll add async support
at some point in the future. No promises. We'll defer that for later.

.. Links
.. -----
.. _clj-http: https://github.com/dakrone/clj-http
.. _http.async.client: https://github.com/neotyk/http.async.client


Links
-----

To use or develop against Rackspace Cloud APIs, we've provided the following
(hopefully) useful links:

* http://docs.rackspace.com/ - documentation for Rackspace Cloud

  * http://docs.rackspace.com/servers/api/v2/cs-devguide/content/ch_preface.html - Cloud Servers docs

* http://www.rackspace.com/cloud/ - information about Rackspace Cloud services

* https://mycloud.rackspace.com/ - sign in to the Rackspace Cloud (OpenStack)
