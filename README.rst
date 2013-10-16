#############
clj-rackspace
#############

*Pure Clojure Bindings for the Rackspace Cloud*


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

This project doesn't really aim to be anything special. As we need some
functionality , we'll add it. There are two efforts driving this work:

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


Vision
------

clj-http-client

.. Links
.. -----


Links
-----

To use or develop against Rackspace Cloud APIs, we've provided the following
(hopefully) useful links:

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
