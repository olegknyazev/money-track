(defproject money-track "0.1.0-SNAPSHOT"
  :description "A web application for personal finance tracking"
  :url "https://github.com/olegknyazev/money-track"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.6.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]]
  :plugins [[lein-ring "0.12.3"]]
  :ring {:handler money-track.core/app})
