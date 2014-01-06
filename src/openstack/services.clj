(ns openstack.services
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [clj-http.client :as http]
            [openstack.const :as const]
            [openstack.exceptions :as exceptions]
            [openstack.util :as util]))


(defn get-service-catalog [identity-response]
  (((util/parse-json-body identity-response) :access) :serviceCatalog))

(defn get-service [identity-response service-type]
  (first
    (filter
      (fn [x] (if (= (x :name) service-type) x))
      (get-service-catalog identity-response))))

