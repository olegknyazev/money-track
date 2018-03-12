(defproject money-track "0.1.0-SNAPSHOT"
  :description "A web application for personal finance tracking"
  :url "https://github.com/olegknyazev/money-track"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.7.3"]
                 [org.postgresql/postgresql "42.2.1.jre7"]
                 [compojure "1.6.0"]
                 [ragtime "0.7.2"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-json "0.4.0"]]
  :plugins [[lein-ring "0.12.3"]]
  :ring {:handler money-track.core/app})
