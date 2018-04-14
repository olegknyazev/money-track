(ns money-track.transaction
  (:require [money-track.data :as data]
            [clojure.java.jdbc :as jdbc]
            [clojure.spec.alpha :as spec]
            [clojure.walk :as walk])
  (:import java.time.LocalDate java.sql.Date))

(def ^:private current-ns-name (str *ns*))

(defn- normalize-date [d]
  (if (instance? String d)
    (.substring d 0 10)
    d))

(defn- normalize-transaction-date [tx]
  (if (contains? tx ::date)
    (assoc tx ::date (sql-date (normalize-date (::date tx))))
    tx))

(defn- qualify-transaction [tx]
  (let [prefix-unqualified
        (fn [x]
          (if (simple-keyword? x)
            (keyword current-ns-name (name x))
            x))]
    (walk/postwalk prefix-unqualified tx)))

(defn transaction [maybe-tx]
  (let [maybe-tx (->> maybe-tx
                      qualify-transaction
                      normalize-transaction-date)
        tx (spec/conform ::transaction maybe-tx)]
    (if-not (spec/invalid? tx)
      tx
      (throw (ex-info "Invalid transaction format"
                      {:type :bad-format
                       :explanation (spec/explain-data ::transaction maybe-tx)})))))

(spec/def ::amount number?)
(spec/def ::merchant string?)
(spec/def ::comment string?)
(spec/def ::transaction (spec/keys :req [::amount ::merchant]
                                   :opt [::comment]))

(defn- min-date [] (LocalDate/of 1990 01 01))
(defn- max-date [] (LocalDate/now))
(defn- sql-date [d] (Date/valueOf d))

(defn- normalize-query-dates [params]
  (assoc params
         :date-from (sql-date (:date-from params (min-date)))
         :date-to (sql-date (:date-to params (max-date)))))

(defn get-transactions
  ([] (get-transactions {}))
  ([params]
   (let [params (normalize-query-dates params)]
     (jdbc/query data/db-spec
                 ["SELECT * FROM transaction WHERE date >= ? AND date <= ?;"
                  (:date-from params)
                  (:date-to params)]))))

(defn update-transaction!
  [id tx]
  (let [[rows-affected]
        (jdbc/update! data/db-spec
                      :transaction
                      (normalize-transaction-date tx)
                      ["id = ?" id])]
    (= rows-affected 1)))

(defn delete-transaction!
  [id]
  (let [[rows-affected]
        (jdbc/delete! data/db-spec
                      :transaction
                      ["id = ?" id])]
    (= rows-affected 1)))

(defn add-transaction! [tx]
  (if-let [tx (transaction tx)]
    (first (jdbc/insert! data/db-spec :transaction tx))
    nil))
