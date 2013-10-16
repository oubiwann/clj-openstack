;;; cloudserver.clj
;;
;; All functions include web service response as metadata.
;;
;; To start out, try:
;;
;; (let [session (login "<username>" "<api key>")
;;       response (list-servers session)]
;;   (println response)
;;   (println (meta response)))
;;
(ns rackspace.cloudserver
  (:use [clojure.contrib.json.read]
  [clojure.contrib.json.write])
  (:require [clojure.http.resourcefully :as net]))

(def constants {:auth-url "https://auth.api.rackspacecloud.com/v1.0"
    :x-auth-user "X-Auth-User"
    :x-server-management-url "X-Server-Management-Url"
    :x-auth-key "X-Auth-Key"
    :x-storage-url "X-Storage-Url"
    :x-auth-token "X-Auth-Token"})

(defstruct rs-response :value)

(defstruct rs-session :url :token)

(defn- basic-get [session arg]
  (try
   (let [response (net/get (str (session :url) arg)
         {(constants :x-auth-token) (session :token)})]
     (with-meta (read-json (first (response :body-seq))) response))
   (catch Exception e (println e))))

(defn login
  "Login and starts a session. Returns an rs-session struct"
  [user auth]
  (try
   (let [response (net/get (constants :auth-url)
         {(constants :x-auth-user) user
          (constants :x-auth-key) auth})]
     (with-meta (struct rs-session
      ((response :headers) (constants :x-server-management-url))
      ((response :headers) (constants :x-auth-token))) response))
   (catch Exception e (println e))))

(defn list-servers
  "Returns running servers. Pass id as an argument to view specific
   server details."
  ([session] (basic-get session "/servers"))
  ([session id] (basic-get session (str "/servers/" id))))

(defn list-flavors
  "Lists machine RAM size choices. Examples: 256MB, 512MB"
  [session]
  ((basic-get session "/flavors") "flavors"))

(defn list-images
  "Lists available machines to launch. Includes Fedora, Gentoo, etc.."
  [session]
  ((basic-get session "/images") "images"))

(defn create-server
  "Creates a new server instance.
   name: Name for server instance.

   image-id: An image's id to build the instance from. Use list-images
   for image options.

   flavor-id: Id of hardware configuration. See choice with list-flavors."
  [session name image-id flavor-id & [metadata]]
  (let [json-vals {:name name
       :imageId image-id
       :flavorId flavor-id}
  server {:server (if (not= nil metadata)
        (-> json-vals (assoc :metadata metadata))
        json-vals)}
  json (json-str server)
  response (net/post (str (session :url) "/servers")
         {(constants :x-auth-token) (session :token)
          "Content-type" "application/json"}
         json)]
    (with-meta ((read-json (first (response :body-seq))) "server") response)))

(defn rename-server
  "Rename server."
  [session server-id name]
  (let [json-vals {:server {:name name}}
  json (json-str json-vals)
  response (net/put (str (session :url) "/servers/" server-id)
        {(constants :x-auth-token) (session :token)
         "Content-type" "application/json"}
        json)]
    (with-meta (struct rs-response (if (= (response :msg) "No Content")
             true
             false)) response)))


(defn delete-server
  "Delete server."
  [session id]
  (let [response (net/delete (str (session :url) "/servers/" id)
           {(constants :x-auth-token) (session :token)})]
    (with-meta (struct rs-response (if (= (response :msg) "Accepted")
             true
             false)) response)))

(defn list-addresses
  "List IPs associated with server id. meta data includes the original
   server response.

   visibility: Either :public, :private or empty for both"
  [session id & [visibility]]
  (let [v (cond (= visibility :public) "/public"
    (= visibility :private) "/private"
    :else nil)
  response (net/get (str (session :url) "/servers/" id "/ips" v)
        {(constants :x-auth-token) (session :token)})]
    (with-meta (read-json (first (response :body-seq))) response)))


(defn- action [session url-args action type action-args]
  (let [response (net/post (str (session :url) url-args)
         {(constants :x-auth-token) (session :token)
          "Content-type" "application/json"}
         (json-str {action {type action-args}}))]
    (with-meta (struct rs-response (if (= (response :msg) "Accepted")
             true
             false)) response)))

(defn reboot-server
  "Reboot server. Type can be either :hard or :soft."
  [session id type]
  (let [t (cond (= type :hard) "HARD"
    (= type :soft) "SOFT"
    :else
    (throw (new Exception "Type argument is not an available type.")))]
    (action session (str "/servers/" id "/action") :reboot :type t)))

(defn rebuild-server
  "Rebuilds server."
  [session id image-id]
  (action session (str "/servers/" id "/action")
    :rebuild :imageId image-id))
