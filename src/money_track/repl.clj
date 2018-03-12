(ns money-track.repl
  (:require [money-track.core :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn start-server []
  (run-jetty (wrap-reload #'app) {:port 3000}))
