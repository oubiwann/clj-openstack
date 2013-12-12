(ns openstack.identity
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [clj-http.client :as http]
            [openstack.const :as const]
            [openstack.exceptions :as exceptions]
            [openstack.util :as util]))


(defn password-auth-payload [username password]
  {:body (json/write-str {:auth
                          {:passwordCredentials
                           {:username username
                            :password password}}})
   :headers {"Content-Type" "application/json"}})

(defn password-login [username password]
  (http/post
    const/auth-url
    (password-auth-payload username password)))

(defn login [username & {:keys [password]}]
  (cond
    password (password-login username password)
    :else (throw
            (exceptions/auth-error
              "AuthError: Missing named parameter"))))

(defn get-disk-username []
  (clojure.string/trim-newline (slurp const/username-file)))

(defn get-disk-password []
  (clojure.string/trim-newline (slurp const/password-file)))

(defn get-env-username
  "Get the user name from the environment variables."
  []
  (util/get-env const/username-env))

(defn get-env-password
  "Get the password from the environment variables."
  []
  (util/get-env const/password-env))

(defn get-env-tenant-name
  "Get the tenant name from the environment variables."
  []
  (util/get-env const/tenant-name-env))

(defn get-env-tenant-id
  "Get the tenant id from the environment variables."
  []
  (util/get-env const/tenant-id-env))

(defn get-env-region-name
  "Get the region name from the environment variables."
  []
  (util/get-env const/region-name-env))

(defn get-env-token
  "Get the token from the environment variables."
  []
  (util/get-env const/token-env))

(defn get-env-auth-url
  "Get the auth url from the environment variables."
  []
  (util/get-env const/auth-url-env))

(defn get-username []
  (let [username (get-env-username)]
  (cond
    (not (empty? username)) username
    :else (get-disk-username))))

(defn get-password
  "Unlike username and apikey, password can technically be an empty string, so
  allowing for that"
  []
  (let [password (get-env-password)]
    (cond
      (not (nil? password)) password
      :else (get-disk-password))))

(defn get-token [response]
  (get-in (util/parse-json-body response) [:access :token :id]))

(defn get-tenant-id [response]
  (get-in (util/parse-json-body response) [:access :token :tenant :id]))

(defn get-user-id [response]
  (get-in (util/parse-json-body response) [:access :user :id]))

(defn get-user-name [response]
  (get-in (util/parse-json-body response) [:access :user :name]))
