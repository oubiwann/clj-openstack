(ns openstack.api
  (:require [openstack.identity :refer [login
                                        get-token]]
            [openstack.services :refer [get-service-catalog
                                        list-cloud-servers-regions
                                        get-cloud-servers-endpoints
                                        get-cloud-servers-region
                                        get-cloud-servers-region-url]]
            [openstack.compute :refer [get-new-server-payload
                                       create-server
                                       get-server-details-list]]))