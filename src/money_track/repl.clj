(ns money-track.repl
  "Provides some functions useful while working in REPL."
  (:require [money-track.core :refer [app]]
            [money-track.data :as data]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ragtime.repl :as rag]))

;; TODO remember current server and add stop-server

(defn start-server []
  (run-jetty (wrap-reload #'app) {:port 3001 :join? false}))

(defn migrate []
  (rag/migrate (data/get-migration-config)))

(defn rollback []
  (rag/rollback (data/get-migration-config)))
