(ns openstack.servers.v2.service-test
  (:require [clojure.test :refer :all]
            [openstack.servers.v2.service :as service]))

(deftest get-new-server-payload-test
  (let [payload (service/get-new-server-payload "server-test" "id-test" "flav-test")]
    (is (= (payload :body)
          "{\"server\":{\"name\":\"server-test\",\"imageRef\":\"id-test\",\"flavorRef\":\"flav-test\"}}"))
    (is (= (payload :headers)
          {"Content-Type" "application/json"}))))
