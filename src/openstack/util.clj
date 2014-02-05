(ns openstack.util
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [openstack.const :as const]))


(defn parse-json-body [response]
  (json/read-str (:body response) :key-fn keyword))

(defn create-temp-file
  ([]
   (create-temp-file "clj-os-" ".tmp"))
  ([prefix suffix]
   (doto (java.io.File/createTempFile prefix suffix) .deleteOnExit)))

(defn make-dirs [path]
  (io/make-parents
    (str path "/" "null")))

(defn create-temp-dir []
  (let [base-dir (System/getProperty "java.io.tmpdir")
        uniq (str (System/currentTimeMillis) "-" (long (rand 1000000)) "-")
        tmp-dir-name (str base-dir "org.clj.openstack." uniq)]
    (make-dirs tmp-dir-name)
    (doto (io/file tmp-dir-name) .deleteOnExit)
    tmp-dir-name))

(defn get-env
  "This is a wrapper method for System/getenv."
  [value]
  (System/getenv value))

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
