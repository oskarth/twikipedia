(ns twikipedia.handler
  (:use compojure.core
        twikipedia.views)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/wiki" [] "Wiki World")
  (GET "/wiki/:page" [page] (wiki-page page))
  (GET "/new" [] (new-page))
  (POST "/new" [:as q] (new-page-post (:params q)))
  (route/resources "/")
  (route/not-found "Not Found"))


(def app
  (handler/site app-routes))
