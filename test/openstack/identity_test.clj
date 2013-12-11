(ns openstack.identity-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [openstack.const :as const]
            [openstack.identity :as identity]
            [openstack.util :as util]
            [openstack.testing.payloads.identity :as payload]))

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
        (identity/login "alice"))))
    ; auth using ENV variables
    ; XXX we'll add a test when we think about how to mock out System/getenv
    ; auth using disc variables
    ; XXX we'll add a test when Sean merges his temp file code
    )

(deftest test-get-token
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "alice" :apikey "0123456789abcdef")
          data (identity/get-token response)]
      (is (= "482664e7cf97408e82f512fad93abc98")))))

(deftest test-get-disk-username
  (let [file (util/create-temp-file)
        file-contents "alice"]
    (spit file file-contents)
    (with-redefs [const/username-file file]
      (let [username (identity/get-disk-username)]
        (is (= username file-contents))))))

(deftest test-get-disk-password
  (let [file (util/create-temp-file)
        file-contents "z0mg11!!secret1!1"]
    (spit file file-contents)
    (with-redefs [const/password-file file]
      (let [password (identity/get-disk-password)]
        (is (= password file-contents))))))

(deftest test-get-disk-apikey
  (let [file (util/create-temp-file)
        file-contents "0a12b33c444d5555ee0123456789ffff"]
    (spit file file-contents)
    (with-redefs [const/apikey-file file]
      (let [apikey (identity/get-disk-apikey)]
        (is (= apikey file-contents))))))

(deftest test-get-env-tenant-name
  (with-redefs [util/get-env (fn [value] "project-1")]
    (is (= (identity/get-env-tenant-name) "project-1"))))

(deftest test-get-env-tenant-id
  (with-redefs [util/get-env (fn [value] "6b8fd2")]
    (is (= (identity/get-env-tenant-id) "6b8fd2"))))

(deftest test-get-env-region-name
  (with-redefs [util/get-env (fn [value] "us-east-2")]
    (is (= (identity/get-env-region-name) "us-east-2"))))

(deftest test-get-env-token
  (with-redefs [util/get-env (fn [value] "e80b74")]
    (is (= (identity/get-env-token) "e80b74"))))

(deftest test-get-env-auth-url
  (with-redefs [util/get-env (fn [value] "http://identity:35322/")]
    (is (= (identity/get-env-auth-url) "http://identity:35322/"))))

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

(deftest test-get-env-username
  (with-redefs [util/get-env (fn [value] "alice")]
    (is (= (identity/get-env-username) "alice"))))

(deftest test-get-env-password
  (with-redefs [util/get-env (fn [value] "secret")]
    (is (= (identity/get-env-password) "secret"))))

(deftest test-get-env-apikey
  (with-redefs [util/get-env (fn [value] "0123456789abcdef")]
    (is (= (identity/get-env-apikey) "0123456789abcdef"))))

(deftest test-get-tenant-id
  (is (= (identity/get-tenant-id payload/login) "007007")))

(deftest test-get-user-id
  (is (= (identity/get-user-id payload/login) "16802")))

(deftest test-get-user-name
  (is (= (identity/get-user-name payload/login) "oubiwann")))
