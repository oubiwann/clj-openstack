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

(defn explicit-login [username & {:keys [password apikey]}]
  (cond
    password (password-login username password)
    apikey (apikey-login username apikey)
    :else (throw
            (exceptions/auth-error
              "AuthError: Missing named parameter"))))

(defn get-disk-username []
  (clojure.string/trim-newline (slurp const/username-file)))

(defn get-disk-password []
  (clojure.string/trim-newline (slurp const/password-file)))

(defn get-disk-apikey []
  (clojure.string/trim-newline(slurp const/apikey-file)))

(defn get-env-username
  "Get the user name from the environment variables."
  []
  (util/get-env const/username-env))

(defn get-env-password
  "Get the password from the environment variables."
  []
  (util/get-env const/password-env))

(defn get-env-apikey
  "Get the API key from the environment variables."
  []
  (util/get-env const/apikey-env))

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

(defn get-apikey []
  (let [apikey (get-env-apikey)]
    (cond
      (not (empty? apikey)) apikey
      :else (get-disk-apikey))))

(defn get-password-or-apikey
  "If a valid password exists return it, else return the API key."
  []
  (or (get-env-apikey) (get-env-password)))

(defn login
  ([]
   (let [apikey (get-apikey)]
     (cond
       (not (empty? apikey))
            (explicit-login (get-username) :apikey apikey)
       :else (explicit-login (get-username) :password (get-password)))))
  ([& data]
    (apply explicit-login data)))

(defn get-token [response]
  (get-in (util/parse-json-body response) [:access :token :id]))

(defn get-tenant-id [response]
  (get-in (util/parse-json-body response) [:access :token :tenant :id]))

(defn get-user-id [response]
  (get-in (util/parse-json-body response) [:access :user :id]))

(defn get-user-name [response]
  (get-in (util/parse-json-body response) [:access :user :name]))
