(ns rackspace.auth
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [clj-http.client :as http]
            [rackspace.const :as const]))


(defn auth-payload [username password]
  {:body (json/write-str {:auth
                          {:passwordCredentials
                           {:username username
                            :password password}}})
   :headers {"Content-Type" "application/json"}})

(defn login [username password]
  (http/post
    const/auth-url
    (auth-payload username password)))

(defn parse-json-body [response]
  (json/read-str (response :body) :key-fn keyword))

(defn get-token [response]
  (((parse-json-body response) :access) :token))

(defn get-service-catalog [response]
  (((parse-json-body response) :access) :serviceCatalog))

(defn get-service [response service-type]
  (first
    (filter
      (fn [x] (if (= (x :name) service-type) x))
      (get-service-catalog response))))

(defn get-cloud-servers-v1 [response]
  (get-service response (const/services :servers-v1)))

(defn get-cloud-servers-v2 [response]
  (get-service response (const/services :servers-v2)))

(defn get-cloud-servers [response & {:keys [version] :or {versoin 2}}]
  (if (= version 1)
    (get-cloud-servers-v1 response)
    (get-cloud-servers-v2 response)))

(defn get-cloud-servers-endpoints [response & {:keys [version] :or {versoin 2}}]
  ((get-cloud-servers response :version version) :endpoints))

(defn list-cloud-servers-regions
  "This is only valid for cloud servers v2."
  [response]
  (map
    (fn [x] (keyword (string/lower-case (x :region))))
    (get-cloud-servers-endpoints response :version 2)))

(defn get-region
  "This is only valid for cloud servers v2."
  [response region]
  (first
    (filter
      (fn [x] (if (= (x :region) (const/regions region)) x))
      (get-cloud-servers-endpoints response :version 2))))

(defn get-region-url [response region]
  ((get-region response region) :publicURL))