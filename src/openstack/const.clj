(ns openstack.const)


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
(def username-env "OS_USERNAME")
(def password-env "OS_PASSWORD")
; TODO: Remove this once the logic has been removed from identity and its tests
(def apikey-env "RAX_APIKEY")
(def tenant-name-env "OS_TENANT_NAME")
(def tenant-id-env "OS_TENANT_ID")
(def region-name-env "OS_REGION_NAME")
(def token-env "OS_TOKEN")
(def auth-url-env "OS_AUTH_URL")

(def server-path "/servers")
(def server-detail-path "/servers/detail")
