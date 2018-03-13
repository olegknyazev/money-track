(ns money-track.data
  (:require [ragtime.jdbc]))

(def db-spec {:dbtype "postgresql"
              :dbname "money-track"
              :host "localhost"
              :user "money-track"
              :password "money-track"})

(defn get-migration-config []
  {:datastore (ragtime.jdbc/sql-database db-spec)
   :migrations (ragtime.jdbc/load-resources "migrations")})

