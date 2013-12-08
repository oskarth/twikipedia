(ns twikipedia.views
  (:use [hiccup core page element]))

;; (def db (atom {}))

(defn save-data []
  (spit "data.db" (prn-str @db)))

(defn load-data []
  (reset! db (read-string (slurp "data.db"))))

(save-data)
(load-data)

(def lorem (:text (:cell-cycle @db)))

(def cell-cycle-img
  [:img
   {:src "/img/cell-cycle.png" :width "300" :height "300" :align "right"}])

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
   [:div [:h1 "The Cell Cycle"]]
    cell-cycle-img
    [:section ;; this is for the editable
     [:div {:contenteditable "true"}
      lorem]]
    [:div {:align "right"}
     ;;[:button {:name "btn"} "Edit"]
    ;;[:button {:name "save"} "Save"]
     ]))) ;; add action here

(defn page-in-db? [page]
  (not (nil? ((keyword page) @db))))

(defn wiki-html [page]
  (html5
   [:head (include-css "/css/style.css")]
   [:body
    [:div#topbar
     [:h1 (link-to "/" "TWIKIPEDIA")]]
    [:div#wrap {:margin-top "-1em"}
     [:h2 "The Cell Cycle"]
     [:hr]
     [:img
      {:src "/img/cell-cycle.png"
       :width "300" :height "300"
       :align "right"}]
     lorem
     [:hr]
     [:div#underbar "short and sweet, once again they meet"]]])) ;; toggle contenteditable
;; "brevis esse laboro, obscurus fio"
;; one of thirty two
;; a book has barely any state at all. what page you are at, and that is it.
;; the rest is yours.
;; imagine with a keyboard. "connected" with others.

(defn wiki-page [page]
  (if (page-in-db? page)
    (wiki-html (str ((keyword page) @db)))
    "New page."))
