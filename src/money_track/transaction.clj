(ns money-track.transaction
  (:require [money-track.data :as data]
            [clojure.java.jdbc :as jdbc]))

(defn get-transactions []
  (jdbc/query data/db-spec "SELECT * FROM transaction;"))

(defn add-transaction! [t]
  (jdbc/insert! data/db-spec :transaction t))

(defn transaction? [t]
  (and (map? t)
       (every? #(contains? t %) [:amount :merchant])))
