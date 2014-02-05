(ns openstack.compute
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [openstack.const :as const]
            [openstack.identity :as identity]
            [openstack.services :as services]
            [openstack.util :as util]))

(defn -get-data
  "Returns data from cloud server based on region and type."
  [identity-response url-path region]
  (let [base-url (services/get-cloud-servers-region-url
                  identity-response
                  region)
        url (str base-url url-path)]
    (util/parse-json-body
     (http/get
      url
      {:headers
       {const/x-auth-token (identity/get-token identity-response)}}))))

(defn -get-list-by-type
  "Formats a request based on request-type and region to be used by -get-data."
  [identity-response request-type region]
  (map
   #(hash-map (:name %1) (:id %1))
   ((keyword request-type) (-get-data
                            identity-response
                            (str "/" request-type)
                            region))))

(defn get-flavors-list
  "Returns a list of all available server flavors for a given region."
  [identity-response region]
  (-get-list-by-type
   identity-response "flavors" (keyword region)))

(defn get-images-list
  "Returns a list of all available server images for a given region."
  [identity-response region]
  (-get-list-by-type
   identity-response  "images" (keyword region)))

(defn get-new-server-payload
  "Creates a string representing a JSON create server request."
  [server-name image-id flavor-id]
  {:body (json/write-str {:server
                          {:name server-name
                           :imageRef image-id
                           :flavorRef flavor-id}})
   :content-type :json})

(defn create-server [identity-response region server-name image-id flavor-id]
  (let [base-url (services/get-compute-url identity-response)]
    (http/post
     (str base-url const/server-path)
     {:content-type :json
      :headers {const/x-auth-token (identity/get-token identity-response)}})))

(defn get-server-list [identity-response region]
  (let [base-url (services/get-compute-url identity-response)]
    (http/get
     (str base-url const/server-detail-path)
     {:accept :json
      :headers {const/x-auth-token (identity/get-token identity-response)}})))
