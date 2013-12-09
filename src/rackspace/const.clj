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

(def rax-dir (str (System/getProperty "user.home") "/.rax"))
(def username-file (str rax-dir "/username"))
(def password-file (str rax-dir "/password"))
(def apikey-file (str rax-dir "/apikey"))
(def username-env "RAX_USERNAME")
(def password-env "RAX_PASSWORD")
(def apikey-env "RAX_APIKEY")