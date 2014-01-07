(ns openstack.api
  (:require [openstack.identity :refer [login
                                        get-token]]
            [openstack.compute :refer [get-new-server-payload
                                       create-server
                                       get-server-list]]
            [openstack.services :refer [get-service-catalog]]
            [openstack.util :as util]))


;; Perform start-up tasks
(util/make-config)
