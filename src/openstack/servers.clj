(ns openstack.servers
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [openstack.const :as const]))

; XXX NOTE!!!
; This file is basically useless right now.
; It is a vestiage of the original code from which this project was cloned.
; This file will slowly have its parts updated, but for now, consider it
; completely broken!

(defstruct rs-response :value)

(defstruct rs-session :url :token)

(defn- basic-get [session arg]
  (try
   (let [response (http/get (str (session :url) arg)
         {const/x-auth-token (session :token)})]
     (with-meta (json/read-str (first (response :body-seq))) response))
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
        json-data (json/write-str server)
        response (http/post (str (session :url) "/servers")
                  {const/x-auth-token (session :token)
          "Content-type" "application/json"}
         json-data)]
    (with-meta ((json/read-str (first (response :body-seq))) "server") response)))

(defn rename-server
  "Rename server."
  [session server-id name]
  (let [json-vals {:server {:name name}}
  json-data (json/write-str json-vals)
  response (http/put (str (session :url) "/servers/" server-id)
        {const/x-auth-token (session :token)
         "Content-type" "application/json"}
        json-data)]
    (with-meta (struct rs-response (if (= (response :msg) "No Content")
             true
             false)) response)))

(defn delete-server
  "Delete server."
  [session id]
  (let [response (http/delete (str (session :url) "/servers/" id)
           {const/x-auth-token (session :token)})]
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
  response (http/get (str (session :url) "/servers/" id "/ips" v)
        {const/x-auth-token (session :token)})]
    (with-meta (json/read-str (first (response :body-seq))) response)))


(defn- action [session url-args action type action-args]
  (let [response (http/post (str (session :url) url-args)
         {const/x-auth-token (session :token)
          "Content-type" "application/json"}
         (json/write-str {action {type action-args}}))]
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
