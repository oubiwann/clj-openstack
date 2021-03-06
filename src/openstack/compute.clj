(ns openstack.compute
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [openstack.const :as const]
            [openstack.identity :as identity]
            [openstack.services :as services]))

(defn get-new-server-payload [server-name image-id flavor-id]
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
