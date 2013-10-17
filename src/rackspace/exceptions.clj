(ns rackspace.exceptions)


(defn exception [message & {:keys [type]}]
  (ex-info message
           {:type type
            :cause message}))

(defn auth-error [message]
  (exception message :type :authentication-error))