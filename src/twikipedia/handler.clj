(ns twikipedia.handler
  (:use compojure.core
        twikipedia.views)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/wiki" [] "Wiki World")
  (GET "/wiki/:page" [page] (str "Welcome to " page))
  ;; everything after this is a url
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
