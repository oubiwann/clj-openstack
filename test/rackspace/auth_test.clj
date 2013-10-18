(ns rackspace.auth-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [rackspace.const :as const]
            [rackspace.auth :as auth]
            [rackspace.testing.payloads.auth :as payload]
            ))


(deftest test-login
  (with-redefs [http/post (fn [url data] payload/login)]
    ; auth with password
    (let [response (auth/login "alice" :password "secret")]
      (is (= [:body :headers :orig-content-encoding
              :request-time :status :trace-redirects]
             (sort (keys response)))))
    ; auth with API key
    (let [response (auth/login "alice" :apikey "0123456789abcdef")]
      (is (= [:body :headers :orig-content-encoding
              :request-time :status :trace-redirects]
             (sort (keys response)))))
    ; attempt auth with incorrect API key parameter
    (is
      (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"AuthError: Missing named parameter"
        (auth/login "alice" :api-key "0123456789abcdef")))
    ; attempt auth with missing named parameter
    (is
      (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"AuthError: Missing named parameter"
        (auth/login "alice")))))

(deftest test-get-token
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/get-token response)]
      (is (= [:RAX-AUTH:authenticatedBy :expires :id :tenant]
             (sort (keys data))))
      (is (= "482664e7cf97408e82f512fad93abc98"
             (data :id))))))

(deftest test-get-service-catalog
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/get-service-catalog response)]
      (is (= 10 (count data))))))

(deftest test-get-cloud-servers-endpoints
  (with-redefs [http/post (fn [url data] payload/login)]
    ; get default version
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/get-cloud-servers-endpoints response)]
      (is (= 4 (count data))))
    ; get version 2 explicitly
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/get-cloud-servers-endpoints response :version 2)]
      (is (= 4 (count data))))
    ; get version 1
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/get-cloud-servers-endpoints response :version 1)]
      (is (= 1 (count data))))))

(deftest test-list-cloud-servers-regions
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/list-cloud-servers-regions response)]
      (is (= [:dfw :iad :ord :syd] (sort data))))))

(deftest test-get-cloud-servers-region
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/get-cloud-servers-region response :dfw)]
      (is (= [:publicURL :region :tenantId :versionId :versionInfo :versionList]
             (sort (keys data)))))))

(deftest test-get-cloud-servers-region-url
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (auth/login "alice" :apikey "0123456789abcdef")
          data (auth/get-cloud-servers-region-url response :ord)]
      (is (= "https://ord.servers.api.rackspacecloud.com/v2/007007"
             data)))))
