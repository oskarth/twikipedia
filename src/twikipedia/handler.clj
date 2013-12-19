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
;;   [ring.middleware.json :refer [wrap-json-response]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.adapter.jetty :refer [run-jetty]]
   [compojure.core :refer [defroutes ANY GET POST]])
  (:import [java.net URI URL]))


;; 1. Create a brown dog named Al (POST /dogs)
;; 2. Rename Al to Rover - update (PUT /dogs/1234)
;; 3. Tell me about a particular dog (GET /dogs/1234)
;; 4. Tell me about all dogs (GET /dogs)
;; 5. Delete Rover :( (DELETE /dogs/1234)
;;

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
  ;; what is actually in this thing...
  ;; seems like a html request and then resource.
  ;; 
;; "{:request {:ssl-client-cert nil, :remote-addr
  ;; \"0:0:0:0:0:0:0:1%0\", :scheme :http, :request-method :post,
  ;; :query-string nil, :route-params {}, :content-type
  ;; \"application/json; charset=utf-8\", :uri \"/wiki\", :server-name
  ;; \"localhost\", :params {}, :headers {\"accept-encoding\" \"gzip,
  ;; deflate, compress\", \"user-agent\" \"HTTPie/0.7.2\",
  ;; \"content-type\" \"application/json; charset=utf-8\",
  ;; \"content-length\" \"14\", \"accept\" \"application/json\",
  ;; \"host\" \"localhost:3000\"}, :content-length 14, :server-port
  ;; 3000, :character-encoding \"utf-8\", :body #<HttpInput
 ;; org.eclipse.jetty.server.HttpInput@284876e0>},
  ;; :resource {:put-to-different-url? #<core$constantly$fn__4051 clojure.core$constantly$fn__4051@6ddb6b07>, :put! #<core$constantly$fn__4051 clojure.core$constantly$fn__4051@604fd0e9>, :available-charsets #<core$constantly$fn__4051 clojure.core$constantly$fn__4051@181ca265>, :processable? #<core$constantly$fn__4051 clojure.core$constantly$fn__4051@7fe9a140>, :allowed? #<core$constantly$fn__405

  
  
   ;;:post-redirect true
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
;;  :handle-ok (str id)
)

(defroutes app
  (ANY "/wiki/:id" [id] (entry-resource id))
  (ANY "/wiki" [] list-resource)
  ;;    (ANY "/" [] "Hi.")
  (route/resources "/")
  (route/not-found "Page not found"))


;; doesn't seem to be working
;; now maybe?
(def handler
  (-> app
      (wrap-trace :header :ui)))





;; is this even used?
#_(def handler
  (-> app
      (wrap-params)))
;;      (wrap-json-response)









;; confusing with names...
#_(defn handler [request]
  (response {:foo "bar"}))
#_(def app
  (wrap-json-response handler))






#_(def handler
  (-> app
      (wrap-params)))
























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
