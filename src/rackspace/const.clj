(ns rackspace.const)


(def auth-url "https://identity.api.rackspacecloud.com/v2.0/tokens")
(def x-auth-user "X-Auth-User")
(def x-server-management-url "X-Server-Management-Url")
(def x-auth-key "X-Auth-Key")
(def x-storage-url "X-Storage-Url")
(def x-auth-token "X-Auth-Token")

(def services {:servers-v2 "cloudServersOpenStack"
               :servers-v1 "cloudServers"
               :files-cdn "cloudFilesCDN"
               :files "cloudFiles"
               :databases "cloudDatabases"
               :backup "cloudBackup"
               :block-storage "cloudBlockStorage"
               :dns "cloudDNS"
               :load-balancers "cloudLoadBalancers"
               :monitoring "cloudMonitoring"})

(def regions {:syd "SYD"
              :dfw "DFW"
              :ord "ORD"
              :iad "IAD"})

(def dot-dir "~/.rax")
(def password-file (clojure.string/join "/" [dot-dir "password"]))
