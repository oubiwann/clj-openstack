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

(defn get-disk-username []
  (clojure.string/trim-newline (slurp const/username-file)))

(defn get-disk-password []
  (clojure.string/trim-newline (slurp const/password-file)))

(defn get-disk-apikey []
  (clojure.string/trim-newline(slurp const/apikey-file)))

(defn get-env-username [])

(defn get-env-password [])

(defn get-env-apikey [])

(defn get-username []
  (let [username (get-env-username)]
  (cond
    (not (nil? username)) username
    :else (get-disk-username))))

(defn get-password []
  (let [password (get-env-password)]
    (cond
      (not (nil? password)) password
      :else (get-disk-password))))

(defn get-apikey []
  (let [apikey (get-env-apikey)]
    (cond
      (not (nil? apikey)) apikey
      :else (get-disk-apikey))))
