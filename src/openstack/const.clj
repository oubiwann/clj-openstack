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

(def os-dir (str (System/getProperty "user.home") "/.openstack"))
(def username-file (str os-dir "/username"))
(def password-file (str os-dir "/password"))
(def apikey-file (str os-dir "/apikey"))
(def config-file (str os-dir "/providers.ini"))
(def username-env "OS_USERNAME")
(def password-env "OS_PASSWORD")
(def tenant-name-env "OS_TENANT_NAME")
(def tenant-id-env "OS_TENANT_ID")
(def region-name-env "OS_REGION_NAME")
(def token-env "OS_TOKEN")
(def auth-url-env "OS_AUTH_URL")

(def server-path "/servers")
(def server-detail-path "/servers/detail")
