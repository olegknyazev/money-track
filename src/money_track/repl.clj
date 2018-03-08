(ns money-track.repl
  (:use [money-track.core :only [app]])
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn start-server []
  (run-jetty (wrap-reload #'app) {:port 3000}))
