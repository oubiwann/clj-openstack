(ns openstack.services-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [openstack.const :as const]
            [openstack.identity :as identity]
            [openstack.services :as services]
            [openstack.testing.payloads.identity :as payload]))


(deftest test-get-service-catalog
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :password "secret")
          data (services/get-service-catalog response)]
      (is (= 10 (count data))))))

(deftest test-get-service-catalog-with-no-services-catalog
  (with-redefs [http/post (fn [url data] payload/login-with-no-service-catalog)]
    (let [response (identity/login "alice" :password "secret")
          data (services/get-service-catalog response)]
      (is (= 0 (count data))))))

(deftest test-get-compute-url
  (with-redefs [http/post (fn [url data] payload/login-with-no-service-catalog)]
    ; get default version
    (let [response (identity/login "alice" :password "secret")
          data (services/get-compute-url response)]
      (is (= 0 (count data))))))
