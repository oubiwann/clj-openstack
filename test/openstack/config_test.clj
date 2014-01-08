(ns openstack.config-test
  (:require [clojure.test :refer :all]
            [openstack.config :as config]
            [openstack.const :as const]))


(deftest test-read-config ()
  (with-redefs [const/config-file "test/configs/provider-some-vals.ini"]
    (let [data (config/read-config)]
      (is (= "alice" ((data "tiny-cloud") "username")))
      (is (= "secret" ((data "tiny-cloud") "password"))))))

(deftest test-read-config-empty ()
  (with-redefs [const/config-file "test/configs/provider-empty.ini"]
    (let [data (config/read-config)]
      (is (= {} (data "some-cloud"))))))

(deftest test-read-config-all-vars ()
  (with-redefs [const/config-file "test/configs/provider-all-vals.ini"]
    (let [data (config/read-config)]
      (is (= "alice" ((data "crazy-cloud") "username")))
      (is (= "secret" ((data "crazy-cloud") "password")))
      (is (= "123abc" ((data "crazy-cloud") "apikey")))
      (is (= "http://kray-zee.clo.ud/v2.0/tokens"
             ((data "crazy-cloud") "auth-endpoint")))
      (is (= "Tarzania" ((data "crazy-cloud") "region")))
      (is (= "456def" ((data "crazy-cloud") "tenant-id")))
      (is (= "alice-proj-42" ((data "crazy-cloud") "tenant-name"))))))

(deftest test-read-config-multiple ()
  (with-redefs [const/config-file "test/configs/provider-multiple.ini"]
    (let [data (config/read-config)]
      (is (= "alice" ((data "cloud 1") "username")))
      (is (= "12345" ((data "cloud 2") "password"))))))
