(ns rackspace.util-test
  (:require [clojure.test :refer :all]
            [rackspace.util :as util]
            [rackspace.testing.payloads.simple :as payload]))


(deftest test-parse-json-body
  (let [data (util/parse-json-body payload/simple)]
  (is (= "some data" (data :a-key)))))