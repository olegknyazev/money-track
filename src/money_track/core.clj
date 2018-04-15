(ns money-track.core
  (:require [compojure.core :refer [defroutes ANY GET PUT POST DELETE]]
            [compojure.route :as route]
            [cheshire.generate :refer [add-encoder]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.http-response :as response]
            [money-track.transaction :as tx])
  (:gen-class))

(add-encoder java.time.Instant
             (fn [inst jsonGenerator]
               (.writeString jsonGenerator (str inst))))

(defroutes app-routes
  (ANY "/ping" req
       (response/ok req))
  (GET "/transaction" req
       (response/ok (tx/get-transactions (:params req))))
  (PUT "/transaction/:id" [id :as {tx :body}]
       (if (tx/update-transaction! (Integer/valueOf id) (tx/transaction tx))
         (response/ok)
         (response/not-found)))
  (DELETE "/transaction/:id" [id]
          (if (tx/delete-transaction! (Integer/valueOf id))
            (response/ok)
            (response/not-found)))
  (POST "/transaction/new" {tx :body}
        (response/ok (tx/add-transaction! (tx/transaction tx)))))

(defn- wrap-exceptions [handler]
  (fn [request]
    (try
      (handler request)
      (catch clojure.lang.ExceptionInfo e
        (let [data (ex-data e)]
          (if (= (:type data) :bad-format)
            (response/bad-request {:message (.getMessage e)
                                   :data data}))))
      (catch Exception e
        (response/internal-server-error (.getMessage e))))))

(def app
  (-> app-routes
      wrap-keyword-params
      wrap-params
      wrap-exceptions
      wrap-json-response
      (wrap-json-body {:keywords? true})))

