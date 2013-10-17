(ns rackspace.api
  (:require [rackspace.auth :refer [login
                                    get-token
                                    list-cloud-servers-regions
                                    get-cloud-servers-endpoints
                                    get-cloud-servers-region
                                    get-cloud-servers-region-url]]))