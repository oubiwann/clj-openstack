(ns rackspace.services-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [rackspace.const :as const]
            [rackspace.identity :as identity]
            [rackspace.services :as services]
            [rackspace.testing.payloads.identity :as payload]))


(deftest test-get-service-catalog
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (services/get-service-catalog response)]
      (is (= 10 (count data))))))

(deftest test-get-cloud-servers-endpoints
  (with-redefs [http/post (fn [url data] payload/login)]
    ; get default version
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (services/get-cloud-servers-endpoints response)]
      (is (= 4 (count data))))
    ; get version 2 explicitly
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (services/get-cloud-servers-endpoints response :version 2)]
      (is (= 4 (count data))))
    ; get version 1
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (services/get-cloud-servers-endpoints response :version 1)]
      (is (= 1 (count data))))))

(deftest test-list-cloud-servers-regions
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (services/list-cloud-servers-regions response)]
      (is (= [:dfw :iad :ord :syd] (sort data))))))

(deftest test-get-cloud-servers-region
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (services/get-cloud-servers-region response :dfw)]
      (is (= [:publicURL :region :tenantId :versionId :versionInfo :versionList]
             (sort (keys data)))))))

(deftest test-get-cloud-servers-region-url
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (services/get-cloud-servers-region-url response :ord)]
      (is (= "https://ord.servers.api.rackspacecloud.com/v2/007007"
             data)))))
