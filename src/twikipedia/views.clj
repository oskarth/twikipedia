(ns twikipedia.views
  (:use [hiccup core page]))

(defn index-page []
  (html5
   [:head
    [:title "Hi"
     (include-css "/css/style.css")]
    [:body
     [:h1 "Hi"]]]))
