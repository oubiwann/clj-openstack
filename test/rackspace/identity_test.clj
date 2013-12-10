(ns rackspace.identity-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [rackspace.const :as const]
            [rackspace.identity :as identity]
            [rackspace.util :as util]
            [rackspace.testing.payloads.identity :as payload]))

; (deftest test-password-auth-payload
;   )

; (deftest test-apikey-auth-payload
;   )

; These are integration tests
; (deftest test-password-login
;   )

; (deftest test-apikey-login
;   )

; (deftest test-explicit-login
;   )

; (deftest test-login
;   )

; (deftest test-login-username-password
;   (with-redefs [http/post (fn [url data] payload/login)]
;     (let [response (identity/login "test-constant-alice" :password "test-constant-secret")]
;       (is (= [:body :headers :orig-content-encoding
;               :request-time :status :trace-redirects]
;              (sort (keys response)))))))

; (deftest test-login-username-apikey
;   (with-redefs [http/post (fn [url data] payload/login)]
;     (let [response (identity/login "test-constant-alice" :apikey "test-constant-0123456789abcdef")]
;       (is (= [:body :headers :orig-content-encoding
;               :request-time :status :trace-redirects]
;              (sort (keys response)))))))

(deftest test-login-failure
  (with-redefs [http/post (fn [url data] payload/login)]
    ; attempt auth with incorrect API key parameter
    (is
      (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"AuthError: Missing named parameter"
        (identity/login "test-constant-alice" :api-key "test-constant-0123456789abcdef")))
    ; attempt auth with missing named parameter
    (is
      (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"AuthError: Missing named parameter"
        (identity/login "test-constant-alice")))
    ; auth using ENV variables
    ; XXX we'll add a test when we think about how to mock out util/get-env
    ; auth using disc variables
    ; XXX we'll add a test when Sean merges his temp file code
    ))

; (deftest test-get-disk-username
;   (with-redefs [const/username-file "asdfadfa"]
;     (is (= (identity/get-disk-username) "expected..."))

;   (with-redefs [slurp (fn [filename] "asdasd")]
;     (is (= (identity/get-disk-username) "asdasd"))

;   (with-redefs [http/post (fn [url data] payload/login)]
;     (let [file (util/create-temp-file)
;           file-contents "test-constant-alice"]
;       (spit file file-contents)
;       (with-redefs [const/username-file file]
;         (let [username (identity/get-disk-username)]
;           (is (= username file-contents)))))))))

(deftest test-get-disk-password
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [file (util/create-temp-file)
          file-contents "test-constant-!!secret1!1"]
      (spit file file-contents)
      (with-redefs [const/password-file file]
        (let [password (identity/get-disk-password)]
          (is (= password file-contents)))))))

(deftest test-get-disk-apikey
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [file (util/create-temp-file)
          file-contents "test-constant-0a12b33c444d5555ee0123456789ffff"]
      (spit file file-contents)
      (with-redefs [const/apikey-file file]
        (let [apikey (identity/get-disk-apikey)]
          (is (= apikey file-contents)))))))

(deftest test-get-env-username
  (with-redefs [util/get-env (fn [value] "test-constant-alice")]
    (is (= (identity/get-env-username) "test-constant-alice"))))

(deftest test-get-env-password
  (with-redefs [util/get-env (fn [value] "test-constant-whatever")]
    (is (= (identity/get-env-password) "test-constant-whatever"))))

(deftest test-get-env-apikey
  (with-redefs [util/get-env (fn [value] "test-constant-12345")]
    (is (= (identity/get-env-apikey) "test-constant-12345"))))

(deftest test-get-token
  (is (= (identity/get-token payload/login) "482664e7cf97408e82f512fad93abc98")))

(deftest test-get-tenant-id
  (is (= (identity/get-tenant-id payload/login) "007007")))

(deftest test-get-user-id
  (is (= (identity/get-user-id payload/login) "16802")))

(deftest test-get-user-name
  (is (= (identity/get-user-name payload/login) "oubiwann")))

(deftest test-get-password
  (with-redefs [identity/get-env-password (fn [] "test-constant-env-password")]
    (let [password (identity/get-password)]
      (is (= password "test-constant-env-password"))))

  (with-redefs [identity/get-env-password (fn [] nil)
                identity/get-disk-password (fn [] "test-constant-disk-password")]
    (let [password (identity/get-password)]
      (is (= password "test-constant-disk-password")))))

(deftest test-get-apikey
  (with-redefs [identity/get-env-apikey (fn [] "test-constant-env-apikey")]
    (let [apikey (identity/get-apikey)]
      (is (= apikey "test-constant-env-apikey"))))
  (with-redefs [identity/get-env-apikey (fn [] nil)
                identity/get-disk-apikey (fn [] "test-constant-disk-apikey")]
    (let [apikey (identity/get-apikey)]
      (is (= apikey "test-constant-disk-apikey")))))

(deftest test-get-token
  (with-redefs [http/post (fn [url data] payload/login)]
    (let [response (identity/login "test-constant-alice" :apikey "test-constant-0123456789abcdef")
          data (identity/get-token response)]
      (is (= "test-constant-482664e7cf97408e82f512fad93abc98")))))

