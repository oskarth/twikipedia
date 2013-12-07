(ns twikipedia.views
  (:use [hiccup core page]))

(def db (atom {:foo "lix" :bar "lux"}))

(defn index-page []
  (html5
   [:head
    [:title "Hi"
     (include-css "/css/style.css")]
    [:body
     [:h1 "Hi"]]]))

(defn wiki-page [page]
  ;; page exists? serve that
  ;; if not, view new.
  (if (nil? ((keyword page) @db))
    "New page."
    (str "This is " ((keyword page) @db))))
