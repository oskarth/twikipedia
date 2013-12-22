(ns twikipedia.handler
  (:use
   twikipedia.views)
  (:require
   [compojure.handler :as handler]
   [compojure.route :as route]
   [clojure.java.io :as io]
   [clojure.data.json :as json]
   
   [liberator.core :refer [resource defresource]]
   [liberator.dev :refer [wrap-trace]]
   [cheshire.core :refer [generate-string]]
   [ring.util.response :refer [response]]
   [ring.middleware.json :refer [wrap-json-response]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.adapter.jetty :refer [run-jetty]]
   [compojure.core :refer [defroutes ANY GET POST]])
  (:import [java.net URI URL]))

;; 1. Create a brown dog named Al (POST /dogs)
;; 2. Rename Al to Rover - update (PUT /dogs/1234)
;; 3. Tell me about a particular dog (GET /dogs/1234)
;; 4. Tell me about all dogs (GET /dogs)
;; 5. Delete Rover :( (DELETE /dogs/1234)

;; we hold a entries in this ref
(defonce entries (ref {}))

;;@entries

(def postbox-counter (atom 0))

;; a helper to create a absolute url for the entry with the given id
(defn build-entry-url [request id]
  (URL. (format "%s://%s:%s%s/%s"
                (name (:scheme request))
                (:server-name request)
                (:server-port request)
                (:uri request)
                (str id))))

(defresource list-resource
  :available-media-types ["application/json"]
  ;; how deal with json?
  :allowed-methods [:get :post]
  :post! (fn [ctx] (do (swap! postbox-counter inc)
                      (prn "ctxkeys " (str (keys ctx)))))

   ;;:location #(build-entry-url (get % :request) (get % ::id))
  
   ;;  :post! #(prn "posting" (str (::data %))) ;; side effect
   ;;:post-redirect? true
   ;;:location "list"
   :handle-ok (fn [_] (str "The counter is " @postbox-counter))
   ;;  :handle-ok ::entry
   ;;  :handle-ok "what"
   :handle-not-acceptable "Sorry, not but language."
   )

;; smt like this
;; :handle-ok (fn [_] (format "The counter is %d" @dbg-counter))
;;  :post! (fn [_] (swap! dbg-counter inc))

;; we can PUT GET and DELETE to "dogs/1234"
(defresource entry-resource [id]
  :available-media-types ["application/json"]
  ;;  :allowed-methods [:get :put :delete]
  :post! #(prn "posting" (str (::data %))) ;; side effect
  ;;:post-redirect? true
  ;;:location "entry"
  :handle-ok ::entry
)

(defroutes app
  (ANY "/wiki/:id" [id] (entry-resource id))
  (ANY "/wiki" [] list-resource)
  (ANY "/" [] (index-page))
  (route/resources "/")
  (route/not-found "Page not found"))


(def handler
  (-> app
      (wrap-params)
      (wrap-trace :header :ui)))

;; (wrap-json-response)






;; old site
#_(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/wiki" [] "Wiki World")
  (GET "/wiki/:page" [page] (wiki-page page))
  (GET "/new" [] (new-page))
  (POST "/new" [:as q] (new-page-post (:params q)))
  (route/resources "/")
  (route/not-found "Not Found"))
#_(def app
  (handler/site app-routes))
