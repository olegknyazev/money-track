(defproject money-track "0.1.0-SNAPSHOT"
  :description "A back-end of a web application for personal finance tracking"
  :url "https://github.com/olegknyazev/money-track"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.7.3"]
                 [org.postgresql/postgresql "42.2.1.jre7"]
                 [ragtime "0.7.2"]
                 [compojure "1.6.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [metosin/ring-http-response "0.9.0"]]
  :plugins [[lein-ring "0.12.3"]]
  :ring {:handler money-track.core/app
         :init money-track.core/initialize
         :port 3001})
