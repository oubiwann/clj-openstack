(ns rackspace.services
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [clj-http.client :as http]
            [rackspace.const :as const]
            [rackspace.exceptions :as exceptions]
            [rackspace.util :as util]))


(defn get-service-catalog [identity-response]
  (((util/parse-json-body identity-response) :access) :serviceCatalog))
  
(defn get-service [identity-response service-type]
  (first
    (filter
      (fn [x] (if (= (x :name) service-type) x))
      (get-service-catalog identity-response))))

(defn get-cloud-servers-v1 [identity-response]
  (get-service identity-response (const/services :servers-v1)))

(defn get-cloud-servers-v2 [identity-response]
  (get-service identity-response (const/services :servers-v2)))

(defn get-cloud-servers [identity-response & {:keys [version] :or {version 2}}]
  (if (= version 1)
    (get-cloud-servers-v1 identity-response)
    (get-cloud-servers-v2 identity-response)))

(defn get-cloud-servers-endpoints
  [identity-response & {:keys [version] :or {version 2}}]
  ((get-cloud-servers identity-response :version version) :endpoints))

(defn list-cloud-servers-regions
  "This is only valid for cloud servers v2."
  [identity-response]
  (map
    (fn [x] (keyword (string/lower-case (x :region))))
    (get-cloud-servers-endpoints identity-response :version 2)))

(defn get-cloud-servers-region
  "This is only valid for cloud servers v2."
  [identity-response region]
  (first
    (filter
      (fn [x] (if (= (x :region) (const/regions region)) x))
      (get-cloud-servers-endpoints identity-response :version 2))))

(defn get-cloud-servers-region-url [identity-response region]
  ((get-cloud-servers-region identity-response region) :publicURL))