(ns money-track.transaction
  (:require [money-track.data :as data]
            [clojure.java.jdbc :as jdbc])
  (:import java.time.LocalDate java.sql.Date))

(defn transaction? [t]
  (and (map? t)
       (every? #(contains? t %) [:amount :merchant])))

(defn- min-date [] (LocalDate/of 1990 01 01))
(defn- max-date [] (LocalDate/now))
(defn- sql-date [d] (Date/valueOf d))

(defn- parse-date [d]
  (if (instance? String d)
    (.substring d 0 10)
    d))

(defn- parse-transaction [tx]
  (if (contains? tx :date)
    (assoc tx :date (sql-date (parse-date (:date tx))))
    tx))

(defn- parse-query-dates [params]
  (assoc params
         :date-from (sql-date (:date-from params (min-date)))
         :date-to (sql-date (:date-to params (max-date)))))

(defn get-transactions
  ([] (get-transactions {}))
  ([params]
   (let [params (parse-query-dates params)]
     (jdbc/query data/db-spec
                 ["SELECT * FROM transaction WHERE date >= ? AND date <= ?;"
                  (:date-from params)
                  (:date-to params)]))))

(defn update-transaction!
  [id tx]
  (jdbc/update! data/db-spec
                :transaction
                (parse-transaction tx)
                ["id = ?" id]))

(defn add-transaction! [tx]
  (let [tx (parse-transaction tx)]
    (if tx
      (first (jdbc/insert! data/db-spec :transaction tx))
      nil)))
