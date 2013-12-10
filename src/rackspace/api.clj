(ns rackspace.api
  (:require [rackspace.identity :refer [login
                                        get-token]]
            [rackspace.services :refer [get-service-catalog
                                        list-cloud-servers-regions
                                        get-cloud-servers-endpoints
                                        get-cloud-servers-region
                                        get-cloud-servers-region-url]]
            [rackspace.servers.v2.service :refer [get-new-server-payload]]))
