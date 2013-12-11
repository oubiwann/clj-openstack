(ns openstack.servers.v2.service-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [openstack.identity :as identity]
            [openstack.servers.v2.service :as service]
            [openstack.services :as services]))

(deftest get-new-server-payload-test
  (let [payload (service/get-new-server-payload "server-test" "id-test" "flav-test")]
    (is (= (payload :body)
           "{\"server\":{\"name\":\"server-test\",\"imageRef\":\"id-test\",\"flavorRef\":\"flav-test\"}}"))
    (is (= (payload :content-type)
           :json))))

;simply ensures that the response from the POST is returned
(deftest create-server-test
  (let [mock-response {:mock "response"}]
    (with-redefs [services/get-cloud-servers-region-url (fn [identity-response region] "url")
                  http/post (fn [url data] mock-response)
                  identity/get-token (fn [identity-response] "token")]
      (let [response (service/create-server {} :ord "server-name" "image-id" "flavor-id")]
        (is = (response
               mock-response))))))

;simply ensures that the response from the GET is returned
(deftest get-server-list-test
  (let [mock-response {:mock "response"}]
    (with-redefs [services/get-cloud-servers-region-url (fn [identity-response region] "url")
                  http/get (fn [url data] mock-response)
                  identity/get-token (fn [identity-response] "token")]
      (let [response (service/get-server-list {} :ord)]
        (is = (response
               mock-response))))))
