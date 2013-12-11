(ns openstack.compute-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [openstack.compute :as compute]
            [openstack.identity :as identity]
            [openstack.services :as services]))

(deftest get-new-server-payload-test
  (let [payload (compute/get-new-server-payload "server-test" "id-test" "flav-test")]
    (is (= payload
           "{\"server\":{\"name\":\"server-test\",\"imageRef\":\"id-test\",\"flavorRef\":\"flav-test\"}}"))))

;simply ensures that the response from the POST is returned
(deftest create-server-test
  (let [mock-response {:mock "response"}]
    (with-redefs [services/get-cloud-servers-region-url (fn [identity-response region] "url")
                  http/post (fn [url data] mock-response)
                  identity/get-token (fn [identity-response] "token")]
      (let [response (compute/create-server {} :ord "server-name" "image-id" "flavor-id")]
        (is = (response
               mock-response))))))

;simply ensures that the response from the GET is returned
(deftest get-server-details-list-test
  (let [mock-response {:mock "response"}]
    (with-redefs [services/get-cloud-servers-region-url (fn [identity-response region] "url")
                  http/get (fn [url data] mock-response)
                  identity/get-token (fn [identity-response] "token")]
      (let [response (compute/get-server-list {} :ord)]
        (is = (response
               mock-response))))))
