(ns rackspace.auth
  (:require [clojure.core.strint :as strint]
            [clojure.data.json :as json]
            [clj-http.client :as http]
            [rackspace.const :as const]))


(defn auth-payload [username password]
  {:body (json/write-str {:auth
                          {:passwordCredentials
                           {:username username
                            :password password}}})
   :headers {"Content-Type" "application/json"}})

(defn login [username password]
  (http/post
    const/auth-url
    (auth-payload username password)))
