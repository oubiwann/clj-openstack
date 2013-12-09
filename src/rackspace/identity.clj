(ns rackspace.identity
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [clj-http.client :as http]
            [rackspace.const :as const]
            [rackspace.exceptions :as exceptions]
            [rackspace.util :as util]))


(defn password-auth-payload [username password]
  {:body (json/write-str {:auth
                          {:passwordCredentials
                           {:username username
                            :password password}}})
   :headers {"Content-Type" "application/json"}})

(defn apikey-auth-payload [username apikey]
  {:body (json/write-str {:auth
                          {:RAX-KSKEY:apiKeyCredentials
                           {:username username
                            :apiKey apikey}}})
   :headers {"Content-Type" "application/json"}})

(defn password-login [username password]
  (http/post
    const/auth-url
    (password-auth-payload username password)))

(defn apikey-login [username apikey]
  (http/post
    const/auth-url
    (apikey-auth-payload username apikey)))

(defn login [username & {:keys [password apikey]}]
  (cond
    password (password-login username password)
    apikey (apikey-login username apikey)
    :else (throw
            (exceptions/auth-error
              "AuthError: Missing named parameter"))))

(defn get-token-data [response]
  (((util/parse-json-body response) :access) :token))

(defn get-token [response]
  ((get-token-data response) :id))

(defn get-auth-credentials
  "This returns a hashmap with user credentials set in the OS environment."
  []
  {:username (System/getenv "RAX_USERNAME")
   :password (System/getenv "RAX_PASSWORD")
   :apikey (System/getenv "RAX_APIKEY")})
