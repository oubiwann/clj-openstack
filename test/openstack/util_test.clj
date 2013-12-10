(ns openstack.util-test
  (:require [clojure.test :refer :all]
            [openstack.util :as util]
            [openstack.testing.payloads.simple :as payload]))


(deftest test-parse-json-body
  (let [data (util/parse-json-body payload/simple)]
  (is (= "some data" (data :a-key)))))
