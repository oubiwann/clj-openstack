;;; cloudserver.clj
;; 
;; To start out, try:
;;
;; (def session (login "<username>" "<api key>"))
;; (list-servers session)
;; 
;;

(ns clojure.rackspace.cloudserver
  (:use [clojure.contrib.json.read]
	[clojure.contrib.json.write])
  (:require [clojure.http.resourcefully :as net]))

(def constants {:auth-url "https://auth.api.rackspacecloud.com/v1.0"
		:x-auth-user "X-Auth-User"
		:x-server-management-url "X-Server-Management-Url"
		:x-auth-key "X-Auth-Key"
		:x-storage-url "X-Storage-Url"
		:x-auth-token "X-Auth-Token"})

(defstruct rs-session :url :token)

(defn- basic-get [session arg]
  (try
   (let [response (net/get (str (session :url) arg)
			   {(constants :x-auth-token) (session :token)})]
     (read-json (first (response :body-seq))))
   (catch Exception e (println e))))

(defn login 
  "Login and starts a session. Returns an rs-session struct"
  [user auth]
  (try 
   (let [response (net/get (constants :auth-url)
			   {(constants :x-auth-user) user
			    (constants :x-auth-key) auth})]
     (struct rs-session 
	     ((response :headers) (constants :x-server-management-url))
	     ((response :headers) (constants :x-auth-token))))
   (catch Exception e (println e))))


(defn list-servers 
  "Returns running servers. Pass id as an argument to view specific 
   server details."
  ([session] ((basic-get session "/servers") "servers"))
  ([session id] (basic-get session (str "/servers/" id))))

(defn list-flavors 
  "Lists machine RAM size choices. Examples: 256MB, 512MB"
  [session] 
  ((basic-get session "/flavors") "flavors"))

(defn list-images 
  "Lists available machines to launch. Includes Fedora, Gentoo, etc.."
  [session] 
  ((basic-get session "/images") "images"))

(defn create-server [session name image-id flavor-id & [metadata]]
  (let [json-vals (-> {} (assoc :name name)
		      (assoc :imageId image-id)
		      (assoc :flavorId flavor-id))
	server {:server (if (not= nil metadata)
			  (-> json-vals (assoc :metadata metadata))
			  json-vals)}
	json (json-str server)
	response (net/post (str (session :url) "/servers")
			   {(constants :x-auth-token) (session :token)
			    "Content-type" "application/json"}
			   json)]
    ((read-json (first (response :body-seq))) "server")))

(defn delete-server [session id]
  (let [response (net/delete (str (session :url) "/servers/" id)
			     {(constants :x-auth-token) (session :token)})]
    (if (= (response :msg) "Accepted")
      true
      false)))



