(ns twikipedia.views
  (:use [hiccup core page]))

(def db (atom {:foo "lix" :bar "lux"}))

(defn template [body]
  (html5
   [:head
    [:title "Twikipedia - soul of wit?"
     (include-css "/css/style.css")]
    [:body body]]))

(defn index-page []
  (template
   [:h1 "Hi"]))

(defn page-in-db? [page]
  (not (nil? ((keyword page) @db))))

(defn wiki-page [page]
  (if (page-in-db? page)
    (str ((keyword page) @db))
    "New page."))
