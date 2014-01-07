(ns openstack.util
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [openstack.const :as const]))


(defn parse-json-body [response]
  (json/read-str (response :body) :key-fn keyword))

(defn create-temp-file
  ([] (create-temp-file "clj-rax-" ".tmp"))
  ([prefix suffix] (doto (java.io.File/createTempFile prefix suffix) .deleteOnExit)))

(defn get-env
  "This is a wrapper method for System/getenv."
  [value]
  (System/getenv value))

(defn make-dirs [path]
  (io/make-parents
    (str path "/" "null")))

(defn config-file? []
  (.exists (io/as-file const/config-file)))

(defn config-dir? []
  (.exists (io/as-file const/os-dir)))

(defn make-config-file []
  (let [default-data (list "[example-provider]"
                           "username ="
                           "password ="
                           "auth-endpoint ="
                           "tenant-id ="
                           "tenant-name ="
                           "region ="
                           "apikey =")]
    (with-open [out (io/writer const/config-file)]
      (binding [*out* out]
        (->> default-data (map println) (dorun))))))

(defn make-config []
  (cond
    (not (config-dir?))
      (make-dirs const/os-dir))
  (cond
    (not (config-file?))
      (make-config-file)))
