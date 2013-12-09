(ns rackspace.testing.payloads.identity)

(def login {
  :orig-content-encoding "gzip",
  :trace-redirects ["https://identity.api.rackspacecloud.com/v2.0/tokens"],
  :request-time 1385,
  :status 200,
  :headers {"server" "nginx/0.8.55",
            "via" "1.0 Repose (Repose/2.3.5)",
            "content-type" "application/json",
            "date" "Thu, 17 Oct 2013 14:54:36 GMT",
            "vary" "Accept, Accept-Encoding, X-Auth-Token",
            "front-end-https" "on",
            "content-length" "946",
            "x-newrelic-app-data" "PxQGUF9aDwETVlhSBQgFVUYdFGQHBDcQUQxLA1tMXV1dORYyVBNFDgFCa04sBkpAVR1BMUdDXggDEWFWAEYiEkAOUQFZTF1dXUcVUR9RH1JKAABXUVsJHwFUWU4VAwZWXFQCV1VQWwYJWg5RDxpp",
            "connection" "close"},
  :body (slurp "src/rackspace/testing/payloads/data/identity-body.json")})
