(ns rackspace.api
  (:require [rackspace.auth :refer [login
                                    get-token
                                    list-cloud-servers-regions
                                    get-cloud-servers-endpoints
                                    get-region
                                    get-region-url]]))