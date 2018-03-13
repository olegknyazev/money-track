(ns money-track.transaction
  (:require [money-track.data :as data]
            [clojure.java.jdbc :as jdbc])
  (:import java.time.LocalDate java.sql.Date))

(defn get-transactions
  ([] (get-transactions {}))
  ([params]
   (let [params (extract-date params)]
     (jdbc/query data/db-spec
                 ["SELECT * FROM transaction WHERE date >= ? AND date <= ?;"
                  (:date-from params)
                  (:date-to params)]))))

(defn add-transaction! [t]
  (jdbc/insert! data/db-spec :transaction t))

(defn transaction? [t]
  (and (map? t)
       (every? #(contains? t %) [:amount :merchant])))

(defn extract-date [params]
  (assoc params
         :date-from (Date/valueOf (:date-from params (LocalDate/of 1990 01 01)))
         :date-to (Date/valueOf (:date-to params (LocalDate/now)))))


