(ns money-track.repl
  "Provides some functions useful while working in REPL."
  (:require [money-track.core :as core]
            [money-track.data :as data]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]))

;; TODO remember current server and add stop-server

(defn start-server []
  (core/initialize {:migrate false})
  (run-jetty (wrap-reload #'core/app) {:port 3001 :join? false}))

(defn migrate []
  (data/migrate))

(defn rollback []
  (data/rollback))
