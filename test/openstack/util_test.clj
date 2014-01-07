(ns openstack.util-test
  (:require [clojure.test :refer :all]
            [openstack.const :as const]
            [openstack.util :as util]
            [openstack.testing.payloads.simple :as payload]))


(deftest test-parse-json-body
  (let [data (util/parse-json-body payload/simple)]
  (is (= "some data" (data :a-key)))))

(deftest test-make-config
  (let* [dir (util/create-temp-dir)
         os-dir (str dir "/openstack")
         config-file (str os-dir "/providers.ini")]
    (with-redefs [const/os-dir os-dir
                  const/config-file config-file]
      (util/make-config)
      (let [file-contents (slurp config-file)]
        (is (= (str "[example-provider]\nusername =\npassword =\nauth-endpoint "
                    "=\ntenant-id =\ntenant-name =\nregion =\napikey =\n")
               file-contents))))))
