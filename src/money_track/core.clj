(ns money-track.core
  (:require [compojure.core :refer [defroutes ANY GET POST]]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.http-response :as response]
            [money-track.transaction :as tx])
  (:gen-class))

(defroutes app-routes
  (ANY "/ping" req
       (response/ok (dissoc req :body)))
  (GET "/transaction" req
       (response/ok (tx/get-transactions (:params req))))
  (POST "/transaction/new" {t :body}
        (if (tx/transaction? t)
          (do
            (tx/add-transaction! t)
            (response/ok {}))
          (response/bad-request))))

(def app
  (-> app-routes
      wrap-keyword-params
      wrap-params
      wrap-json-response
      (wrap-json-body {:keywords? true})))

