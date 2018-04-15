(ns money-track.data
  (:require [ragtime.jdbc]
            [clojure.java.jdbc :as jdbc]
            [clojure.walk :as walk])
  (:import java.time.Instant java.sql.Timestamp))

(def db-spec {:dbtype "postgresql"
              :dbname "money-track"
              :host "localhost"
              :user "money-track"
              :password "money-track"})

(defn get-migration-config []
  {:datastore (ragtime.jdbc/sql-database db-spec)
   :migrations (ragtime.jdbc/load-resources "migrations")})

(defn- to-sql-value [x]
  (if (instance? Instant x)
    (Timestamp/from x)
    x))

(defn- to-sql [obj]
  (walk/postwalk to-sql-value obj))

(defn from-sql-value [x]
  (if (instance? Timestamp x)
    (.toInstant x)
    x))

(defn from-sql [obj]
  (walk/postwalk from-sql-value obj))

(defn query [query & args]
  (from-sql (jdbc/query db-spec (apply list query (to-sql args)))))

(defn update! [table row where]
  (let [[rows-affected]
        (jdbc/update! db-spec table (to-sql row) (to-sql where))]
    rows-affected))

(defn delete! [table where]
  (let [[rows-affected]
        (jdbc/delete! db-spec table (to-sql where))]
    rows-affected))

(defn insert! [table row]
  (from-sql (jdbc/insert! db-spec table (to-sql row))))
