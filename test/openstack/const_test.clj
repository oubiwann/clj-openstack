(ns openstack.const-test
  (:require [clojure.test :refer :all]
            [openstack.const :as const]))


(deftest test-auth-url
  (is (= "https://identity.api.rackspacecloud.com/v2.0/tokens"
         const/auth-url)))

(def test-x-auth-user
  (is (= "X-Auth-User"
         const/x-auth-user)))

(def test-x-server-management-url
  (is (= "X-Server-Management-Url"
        const/x-server-management-url)))

(def test-x-auth-key
  (is (= "X-Auth-Key"
        const/x-auth-key)))

(def test-x-storage-url
  (is (= "X-Storage-Url"
        const/x-storage-url)))

(def test-x-auth-token
  (is (= "X-Auth-Token"
        const/x-auth-token)))

(deftest test-services
  (is (= [:backup :block-storage :databases :dns :files :files-cdn
          :load-balancers :monitoring :servers-v1 :servers-v2]
         (sort (keys const/services))))
  (is (= 10
         (count const/services))))

(deftest test-regions
  (is (= [[:dfw "DFW"] [:iad "IAD"] [:ord "ORD"] [:syd "SYD"]]
         (sort const/regions)))
  (is (= 4
         (count const/regions))))

(deftest test-rax-dir
  (is (= (str (System/getProperty "user.home") "/.rax")
         const/rax-dir)))

(deftest test-username-file
  (is (= (str (System/getProperty "user.home") "/.rax/username")
         const/username-file)))

(deftest test-password-file
  (is (= (str (System/getProperty "user.home") "/.rax/password")
         const/password-file)))

(deftest test-apikey-file
  (is (= (str (System/getProperty "user.home") "/.rax/apikey")
         const/apikey-file)))

(deftest test-username-env
  (is (= "RAX_USERNAME"
         const/username-env)))

(deftest test-password-env
  (is (= "RAX_PASSWORD"
        const/password-env)))

(deftest test-apikey-env
  (is (= "RAX_APIKEY"
        const/apikey-env)))

(deftest test-server-path
  (is (= "/servers"
         const/server-path)))

(deftest test-server-detail-path
  (is (= "/servers/detail"
         const/server-detail-path)))