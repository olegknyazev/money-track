(ns money-track.transaction
  (:require [money-track.data :as data]
            [clojure.java.jdbc :as jdbc])
  (:import java.time.LocalDate java.sql.Date))

(declare extract-date)
(declare coerce-transaction)

(defn get-transactions
  ([] (get-transactions {}))
  ([params]
   (let [params (extract-date params)]
     (jdbc/query data/db-spec
                 ["SELECT * FROM transaction WHERE date >= ? AND date <= ?;"
                  (:date-from params)
                  (:date-to params)]))))

(defn add-transaction! [t]
  (let [t (coerce-transaction t)]
    (if t
      (jdbc/insert! data/db-spec :transaction t)
      nil)))

(defn transaction? [t]
  (and (map? t)
       (every? #(contains? t %) [:amount :merchant])))

(defn- coerce-transaction [t]
  (if (transaction? t)
    (assoc t :date (sql-date (:date t max-date)))
    nil))

(defn- min-date [] (LocalDate/of 1990 01 01))
(defn- max-date [] (LocalDate/now))
(defn- sql-date [d] (Date/valueOf d))

(defn- extract-date [params]
  (assoc params
         :date-from (sql-date (:date-from params (min-date)))
         :date-to (sql-date (:date-to params (max-date)))))


