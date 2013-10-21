(ns rackspace.util
  (:require [clojure.data.json :as json]))


(defn parse-json-body [response]
  (json/read-str (response :body) :key-fn keyword))
