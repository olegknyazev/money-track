(ns money-track.core
  (:require [compojure.core :refer [GET POST]]
            [compojure.route :as route]
            [ring.middleware.json :as middleware.json]
            [ring.util.response :refer [response]]
            [money-track.transaction :as tx])
  (:gen-class))

(defroutes app-routes
  (GET "/transaction" []
       (response (tx/get-transactions)))
  (POST "/transaction/new" {t :body}
        (tx/add-transaction! t)
        (response {})))

(def app
  (-> app-routes
      middleware.json/wrap-json-response
      middleware.json/wrap-json-body))
