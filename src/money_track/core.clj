(ns money-track.core
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.middleware.json :as middleware.json]
            [ring.util.http-response :as response]
            [money-track.transaction :as tx])
  (:gen-class))

(defroutes app-routes
  (GET "/transaction" []
       (response/ok (tx/get-transactions)))
  (POST "/transaction/new" {t :body}
        (if (tx/transaction? t)
          (do
            (tx/add-transaction! t)
            (response/ok {}))
          (response/bad-request)))) 

(def app
  (-> app-routes
      (middleware.json/wrap-json-response)
      (middleware.json/wrap-json-body {:keywords? true})))

