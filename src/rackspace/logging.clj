(ns rackspace.logging "Library-specific wrapper around timbre"
  (:require [taoensso.timbre :as timbre]))

(defn log [level message]
  (timbre/log level message))

(defn debug [message]
  (log :debug message))

(defn error [message]
  (log :error message))

(defn fatal [message]
  (log :fatal message))

(defn info [message]
  (log :info message))

(defn report [message]
  (log :report message))

(defn trace [message]
  (log :trace message))

(defn warn [message]
  (log :warn message))
