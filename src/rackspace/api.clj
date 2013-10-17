(ns rackspace.api
  (:require [rackspace.auth :refer [login
                                    get-token
                                    get-service-catalog
                                    list-cloud-servers-regions
                                    get-cloud-servers-endpoints
                                    get-cloud-servers-region
                                    get-cloud-servers-region-url]]))