(ns rackspace.identity-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [rackspace.const :as const]
            [rackspace.identity :as identity]
            [rackspace.testing.payloads.identity :as payload]))


(deftest test-login
  (with-redefs [http/post (fn [url data] payload/login)]
    ; auth with password
    (let [response (identity/login "alice" :password "secret")]
      (is (= [:body :headers :orig-content-encoding
              :request-time :status :trace-redirects]
             (sort (keys response)))))
    ; auth with API key
    (let [response (identity/login "alice" :apikey "0123456789abcdef")]
      (is (= [:body :headers :orig-content-encoding
              :request-time :status :trace-redirects]
             (sort (keys response)))))
    ; attempt auth with incorrect API key parameter
    (is
      (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"AuthError: Missing named parameter"
        (identity/login "alice" :api-key "0123456789abcdef")))
    ; attempt auth with missing named parameter
    (is
      (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"AuthError: Missing named parameter"
        (identity/login "alice")))))

(deftest test-get-token-data
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (identity/get-token-data response)]
      (is (= [:RAX-AUTH:authenticatedBy :expires :id :tenant]
             (sort (keys data))))
      (is (= "482664e7cf97408e82f512fad93abc98"
             (data :id))))))

(deftest test-get-token
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (identity/get-token response)]
      (is (= "482664e7cf97408e82f512fad93abc98")))))

(deftest test-get-username
  (with-redefs [identity/get-env-username (fn [] "env-username")]
    (let [username (identity/get-username)]
      (is (= username "env-username"))))
  (with-redefs [identity/get-env-username (fn [] nil)
                identity/get-disk-username (fn [] "disk-username")]
    (let [username (identity/get-username)]
      (is (= username "disk-username")))))

(deftest test-get-password
  (with-redefs [identity/get-env-password (fn [] "env-password")]
    (let [password (identity/get-password)]
      (is (= password "env-password"))))
  (with-redefs [identity/get-env-password (fn [] nil)
                identity/get-disk-password (fn [] "disk-password")]
    (let [password (identity/get-password)]
      (is (= password "disk-password")))))

(deftest test-get-apikey
  (with-redefs [identity/get-env-apikey (fn [] "env-apikey")]
    (let [apikey (identity/get-apikey)]
      (is (= apikey "env-apikey"))))
  (with-redefs [identity/get-env-apikey (fn [] nil)
                identity/get-disk-apikey (fn [] "disk-apikey")]
    (let [apikey (identity/get-apikey)]
      (is (= apikey "disk-apikey")))))

(deftest test-get-auth-credentials
  (let [test-creds (identity/get-auth-credentials)]
  (is (not-any? nil? [(test-creds :username)
                  (test-creds :password)
                  (test-creds :apikey)]))))
