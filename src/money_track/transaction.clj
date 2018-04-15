(ns money-track.transaction
  (:require [money-track.data :as data]
            [clojure.spec.alpha :as spec]
            [clojure.walk :as walk])
  (:import java.time.ZonedDateTime java.time.Instant java.sql.Date))

(spec/def ::amount number?)
(spec/def ::merchant string?)
(spec/def ::comment string?)
(spec/def ::date inst?)
(spec/def ::transaction (spec/keys :req-un [::amount ::merchant ::date]
                                   :opt-un [::comment]))

(defn- normalize-dates [tx]
  (if (contains? tx :date)
    (assoc tx :date (Instant/parse (:date tx))) ; TODO parse only if string
    tx))

(defn transaction [maybe-tx]
  (let [maybe-tx (normalize-dates maybe-tx)
        tx (spec/conform ::transaction maybe-tx)]
    (if-not (spec/invalid? tx)
      tx
      (throw (ex-info "Invalid transaction format"
                      {:type :bad-format
                       :explanation (spec/explain-data ::transaction maybe-tx)})))))

(defn- min-date [] (Instant/EPOCH))
(defn- max-date [] (.toInstant (ZonedDateTime/now)))

(defn- normalize-query-dates [params]
  (assoc params
         :date-from (:date-from params (min-date))
         :date-to (:date-to params (max-date))))

(defn get-transactions
  ([] (get-transactions {}))
  ([params]
   (let [params (normalize-query-dates params)]
     (data/query "SELECT * FROM transaction WHERE date >= ? AND date <= ?;"
                  (:date-from params)
                  (:date-to params)))))

(defn update-transaction! [id tx]
  (= 1 (data/update! :transaction
                     tx
                     ["id = ?" id])))


(defn delete-transaction! [id]
  (= 1 (data/delete! :transaction
                     ["id = ?" id])))

(defn add-transaction! [tx]
  (first (data/insert! :transaction tx)))
