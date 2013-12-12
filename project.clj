(defproject clj-openstack "0.0.1-SNAPSHOT"
  :description "Pure Clojure language bindings for OpenStack Clouds"
  :url "https://github.com/oubiwann/clj-openstack"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cheshire "5.2.0"]
                 [clj-http "0.7.7"]
                 [com.taoensso/timbre "3.0.0-RC2"]]
  :repositories [["releases" {:url "https://clojars.org/repo"
                              :creds :gpg}]]
  :repl-options {
                 :init-ns openstack.api}
  :profiles {
             :dev {
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]
                                  [org.clojure/java.classpath "0.2.0"]]}
             :testing {
                       :dependencies [[leiningen "2.3.3"]]}})
