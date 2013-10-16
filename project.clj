(defproject clj-rackspace "0.1.0-SNAPSHOT"
  :description "Pure Clojure language bindings for the Rackspace Cloud"
  :url "https://github.com/oubiwann/clj-rackspace"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {
    :testing {
      :dependencies [[ring-mock "0.1.5"]
                     [clj-http-fake "0.4.1"]
                     [midje "1.5.1"]]}})
