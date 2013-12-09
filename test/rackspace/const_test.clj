(ns rackspace.const-test
  (:require [clojure.test :refer :all]
            [rackspace.const :as const]))


(deftest test-auth-url
  (is (= "https://identity.api.rackspacecloud.com/v2.0/tokens"
         const/auth-url)))

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