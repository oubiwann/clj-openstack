#############
clj-openstack
#############

*Pure Clojure Bindings for OpenStack Clouds*


.. contents:: Table of Contents


Purpose
=======

Fun.

This project doesn't really aim to be anything special. As we need some
functionality, we'll add it. Initial focus will be on supporting only a subset
of Rackspace Cloud Services (version 2 Cloud Servers).


Installation
============

`clj-openstack` is up on `Clojars`_. You can add it to your `project.clj` for
automatic download with the following:

.. code:: clojure

    (defproject your-project "1.2.3"
      ...
      :dependencies [[org.clojure/clojure "1.5.1"]
                      ...
                      [clj-openstack "0.1.1"]]
      ...)

You can then use it in your project like so:

.. code:: clojure

    (ns your-project.client
      (:require [openstack.api :as os-api]))

Or from the REPL:

.. code:: clojure

    (require '[openstack.api :as rax-api])

.. Links
.. -----
.. _Clojars: https://clojars.org/clj-openstack


Running Example Code
====================

The easiest way to get started with ``clj-openstack`` is to play with it in the
REPL. You'll need to download the code for this:

.. code:: bash

    $ git clone https://github.com/oubiwann/clj-openstack
    $ cd clj-openstack

Now just do this at the command line prompt:

.. code:: bash

    $ make shell

Which will dump you in the `openstack.api` namespace:

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

    openstack.api=>

For the examples below, you will need to provide your own username, password,
and any data returned from Rackspace Cloud services.

To make things easier to read in the examples below, let's set up
pretty-printing:

.. code:: clojure

    openstack.api=> (require '[clojure.pprint :refer [pprint]])
    nil
    openstack.api=>


Identity Service Usage
======================

Loging in via password entered directly:

.. code:: clojure

    openstack.api=> (def response (login :username "alice" :password "z0mg1!1"))
    #'openstack.api/response
    openstack.api=>

Via the ``~/.openstack/providers.ini`` configuration file:

.. code:: ini

    [my-cloud]
    username = bob
    password = 12345

.. code:: clojure

    openstack.api=> (login :provider "my-cloud")
    {:orig-content-encoding "gzip" ... }
    openstack.api=>

Extracted from the environment:

.. code:: clojure

    openstack.api=> (login :env true)
    {:orig-content-encoding "gzip" ... }
    openstack.api=>

Extracted from files:

.. code:: clojure

    openstack.api=> (login :files true)
    {:orig-content-encoding "gzip" ... }
    openstack.api=>

Implicit extraction (first env is checked, and then files):

.. code:: clojure

    openstack.api=> (login)
    {:orig-content-encoding "gzip" ... }
    openstack.api=>

Attempting to use a bad keyword or only one of a require keyword pair will throw
an error:

.. code:: clojure

    openstack.api=> (login username: "alice")

    ExceptionInfo AuthError: Missing named parameter  ...
    openstack.api=>


Working with Login Data
-----------------------

With our response data saved, we can now perform several operations with `auth`
utility functions.

Getting the token:

.. code:: clojure

    openstack.api=> (pprint (get-token response))
    {:id "482664e7cf97408e82f512fad93abc98",
     :expires "2013-10-17T20:11:40.557-05:00",
     :tenant {:id "007007", :name "007007"},
     :RAX-AUTH:authenticatedBy ["PASSWORD"]}
    nil
    openstack.api=>


Compute Service Usage
=====================

TBD


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

To use or develop against OpenStack Cloud APIs, we've provided the following
(hopefully) useful links:

* http://docs.openstack.org/api/quick-start/content/ - quick start for OpenStack
  APIs

* http://api.openstack.org/api-ref-identity.html - Login/authentication API docs

* http://api.openstack.org/api-ref-compute.html - Compute API docs
