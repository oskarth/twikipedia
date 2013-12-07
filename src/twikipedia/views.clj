(ns twikipedia.views
  (:use [hiccup core page]))

(def lorem
  "Lorem ipsum dolor sit amet, consectetur adipiscing
  elit. Morbi ut lorem nec enim aliquet placerat id tincidunt
  massa. Integer sit amet nibh ac neque vestibulum rutrum. Aliquam
  venenatis tincidunt lectus nec hendrerit. Aliquam vestibulum volutpat
  commodo. Praesent volutpat, diam vitae imperdiet semper, dui nibh
  sodales ante, quis sagittis massa felis a eros. Vivamus eu elit vitae
  felis pharetra facilisis. Sed dolor libero, dapibus a turpis at,
  mollis lacinia odio. Aenean bibendum felis ipsum, quis ullamcorper
  lacus tempus ut. Quisque lectus sapien, egestas vitae enim a, cursus
  pharetra dui.

Etiam porta, sapien sit amet feugiat scelerisque, nunc sapien mattis
lorem, id eleifend nunc libero eget neque. Sed quis vulputate
magna. Duis eu lacinia ligula. Etiam condimentum neque ut sapien
eleifend commodo. Pellentesque tincidunt ante sem, nec tempor quam
tempus at. Aliquam et varius mauris. Nulla tempor eros vel eleifend
congue. Cras non eleifend libero. Proin pellentesque commodo lectus
amet.")

(def db (atom {:foo "lix" :bar "lux"}))

(defn template [body]
  (html
   [:head
    [:title "Twikipedia - soul of wit?"]
    (include-css "/css/style.css")
    [:body
     [:div#wrapper body]]]))

(defn index-page []
  (template
   (html5
    [:h1 "Twikipedia"]
    [:section
     [:div {:contenteditable "true"}
      lorem]])))

(defn page-in-db? [page]
  (not (nil? ((keyword page) @db))))

(defn wiki-page [page]
  (if (page-in-db? page)
    (str ((keyword page) @db))
    "New page."))
