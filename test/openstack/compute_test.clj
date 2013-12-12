(ns openstack.compute-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [openstack.compute :as compute]
            [openstack.identity :as identity]
            [openstack.services :as services]
            [openstack.testing.payloads.compute :as payload]
            [openstack.util :as util]))

(defn mock-identity-response [identity-response] "token")

(deftest -get-data-test
  "Simply ensures that the response from the GET is returned."
  (let [mock-response {:body "{\"mock\": \"response\"}"}]
    (with-redefs [http/get (fn [url data] mock-response)
                  identity/get-token mock-identity-response
                  services/get-cloud-servers-region-url (fn [identity-response region] "url")]
    (let [response (compute/-get-data "auth" "url-path" :dfw)]
      (is (not (nil? response)))))))

(deftest -get-list-by-type-test
  "Ensures that the response for flavors returns correctly."
  (with-redefs [compute/-get-data (fn [identity-response url-path region] payload/flavors)]
    (let [response (util/parse-json-body (compute/-get-data "auth" "flavors" :dfw))
          response-keys (sort (keys response))]
      (is (= response-keys '[:flavors])))))

(deftest get-flavors-list-test
  (with-redefs [compute/get-flavors-list (fn [identity-response region] payload/flavors)]
    (let [response (util/parse-json-body (compute/get-flavors-list "auth" :dfw))
          response-keys (sort (keys response))]
      (is (= response-keys '[:flavors])))))

(deftest get-images-list-test
  (with-redefs [compute/get-images-list (fn [identity-response region] payload/images)]
    (let [response (util/parse-json-body (compute/get-images-list "auth" :dfw))
          response-keys (sort (keys response))]
      (is (= response-keys '[:images])))))

(deftest get-new-server-payload-test
  (let [payload (compute/get-new-server-payload "server-test" "id-test" "flav-test")]
    (is (= (payload :body)
           "{\"server\":{\"name\":\"server-test\",\"imageRef\":\"id-test\",\"flavorRef\":\"flav-test\"}}"))
    (is (= (payload :content-type)
           :json))))

(deftest create-server-test
  "Simply ensures that the response from the POST is returned."
  (let [mock-response {:mock "response"}]
    (with-redefs [services/get-cloud-servers-region-url (fn [identity-response region] "url")
                  http/post (fn [url data] mock-response)
                  identity/get-token mock-identity-response]
      (let [response (compute/create-server {} :ord "server-name" "image-id" "flavor-id")]
        (is = (response
               mock-response))))))

(deftest get-server-list-test
  "Simply ensures that the response from the GET is returned."
  (let [mock-response {:mock "response"}]
    (with-redefs [services/get-cloud-servers-region-url (fn [identity-response region] "url")
                  http/get (fn [url data] mock-response)
                  identity/get-token mock-identity-response]
      (let [response (compute/get-server-list {} :ord)]
        (is = (response
               mock-response))))))
