(ns rackspace.util
  (:require [clojure.data.json :as json]))


(defn parse-json-body [response]
  (json/read-str (response :body) :key-fn keyword))

(defn nil-or-val
  "This returns nil if the value is an empty string."
  [value]
  (if (= value "") nil value))
