(ns openstack.config
  (:require [com.brainbot.iniconfig :as iniconfig]
            [openstack.const :as const]))


(defn read-config
  ([]
    (read-config const/config-file))
  ([config-file]
    (iniconfig/read-ini config-file)))
