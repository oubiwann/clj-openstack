(ns rackspace.const-test
  (:require [clojure.test :refer :all]
            [rackspace.const :as const]))


(deftest test-auth-url
  (is (= "https://identity.api.rackspacecloud.com/v2.0/tokens"
         const/auth-url)))

(deftest test-services
  (is (= [:backup :block-storage :databases :dns :files :files-cdn
          :load-balancers :monitoring :servers-v1 :servers-v2]
         (sort (keys const/services)))))

(deftest test-regions
  (is (= [[:dfw "DFW"] [:iad "IAD"] [:ord "ORD"] [:syd "SYD"]]
         (sort const/regions))))

(deftest test-password-file
  (is (= (str (System/getProperty "user.home") "/.rax/password") const/password-file)))