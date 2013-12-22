(ns twikipedia.core
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

;; Almost like a kata
;; 1. Create a brown dog named Al (POST /dogs)
;; 2. Rename Al to Rover - update (PUT /dogs/1234)
;; 3. Tell me about a particular dog (GET /dogs/1234)
;; 4. Tell me about all dogs (GET /dogs)
;; 5. Delete Rover :( (DELETE /dogs/1234)

;; Import database stuff from views file
(def db (atom {}))
(defn load-data []
  (reset! db (read-string (slurp "data.db"))))

(load-data)

(defn keywordify [title] ;; How To Make Scrambled Eggs -> how-to-make-scrambled-eggs
  (keyword (clojure.string/replace (clojure.string/lower-case title) " " "-")))

(defn save-data []
  (spit "data.db" (prn-str @db)))

(defn add! [title src text]
  (swap! db assoc (keywordify title)
         {:title title
          :src src
          :text text})
  (save-data))

(add! "How To Make Scrambled Eggs"
      "/img/scrambled-eggs.png"
      "The most important thing about scrambled eggs is stopping them from overcooking. Start off with eggs in the pan and some butter. Don't salt or whisk the eggs before they get into your pan. Use a spatula. Start on a generous heat. Give them a break from the heat once they get going, so they can combine and avoid drying out, repeat three or four times. Continue stirring, it's a live thing. When it starts to get together, take it off. Put creme fraiche to cool it. Season with salt, peppar and a touch of chives.")

;;(:how-to-make-scrambled-eggs @db)

;;(load-data)


#_(swap! db (assoc :scrambled-eggs
            {:title "How To Make Scrambled Eggs"
             :src "/img/scrambled-eggs.png"
             :text "The most important thing about scrambled eggs is stopping them from overcooking. Start off with eggs in the pan and some butter. Don't salt or whisk the eggs before they get into your pan. Use a spatula. Start on a generous heat. Give them a break from the heat once they get going, so they can combine and avoid drying out, repeat three or four times. Continue stirring, it's a live thing. When it starts to get together, take it off. Put creme fraiche to cool it. Season with salt, peppar and a touch of chives."}))

;; how should I store git WIPs?
;; also no forget devop

;; O(n) notation explain




;;@entries

(defn get-entry [id]
  "foo")

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
  :allowed-methods [:get :post]
  :post! (fn [ctx] (do (swap! postbox-counter inc)
                      (prn "ctxkeys " (str (keys ctx)))))
   :handle-ok (fn [_] (str "The counter is " @postbox-counter))
   )

;; :handle-ok (fn [_] (format "The counter is %d" @dbg-counter))
;;  :post! (fn [_] (swap! dbg-counter inc))

;; get should return something.

;; we can PUT GET and DELETE to "dogs/1234"
(defresource entry-resource [id]
  :available-media-types ["application/json"]
    :allowed-methods [:get :put :delete]
  :post! #(prn "posting" (str (::data %))) ;; side effect
  :handle-ok (get-entry id)) ;; is this ok?

(defroutes app
  (ANY "/wiki/:id" [id] (entry-resource id))
  (ANY "/wiki" [] list-resource)
  (ANY "/" [] (index-page))
  (route/resources "/")
  (route/not-found "Page not found"))


(def handler
  (-> app
      (wrap-params)
      ))
;;(wrap-trace :header :ui)

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
