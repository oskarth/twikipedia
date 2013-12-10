(ns twikipedia.views
  (:use [hiccup core page element form]))

(def db (atom {}))
(defn load-data []
  (reset! db (read-string (slurp "data.db"))))

(load-data)

(defn save-data []
  (spit "data.db" (prn-str @db)))

;;(save-data)
;;(load-data)


#_(swap! db (assoc :scrambled-eggs
            {:title "How To Make Scrambled Eggs"
             :src "/img/scrambled-eggs.png"
             :text "The most important thing about scrambled eggs is stopping them from overcooking. Start off with eggs in the pan and some butter. Don't salt or whisk the eggs before they get into your pan. Use a spatula. Start on a generous heat. Give them a break from the heat once they get going, so they can combine and avoid drying out, repeat three or four times. Continue stirring, it's a live thing. When it starts to get together, take it off. Put creme fraiche to cool it. Season with salt, peppar and a touch of chives."}))

;; how should I store git WIPs?
;; also no forget devop

;; O(n) notation explain



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
     [:div#topbar
     [:h1 (link-to "/" "TWIKIPEDIA")]]
     [:div#wrap body
      [:hr]
      [:div#underbar "short and sweet, once again they meet"]]
     ]]))

(defn index-page []
  (template
   (html
    "Not wikipedia, nor a dictionary. We like brevity, examples and images;
dislike opaqueness and sticklers. Pour a glass and have a browse!"
    [:h2
     [:ol
      [:li (link-to "/wiki/cell-cycle" "The Cell Cycle")]
      [:li (link-to "/wiki/apoptosis" "Apoptosis")]
      [:li (link-to "/wiki/bistability" "Bistabilty")]
      [:li (link-to "/wiki/bifurcation" "Bifurcation")]
      [:li (link-to "/wiki/how-to-make-scrambled-eggs" "How To Make Scrambled Eggs")]]]
    [:p "Or make a new page at " (link-to "/new" "here") "."])))

(defn page-in-db? [page]
  (not (nil? ((keyword page) @db))))

(defn wiki-html [page]
  (template
   (html5
    [:body
     [:div#wrap {:margin-top "-1em"}
      [:h2 "The Cell Cycle"]
      [:hr]
      [:img
       ;; where does width height align info belong?
       {:src "/img/cell-cycle.png"
        :width "300" :height "300"
        :align "right"}]
      lorem]])))
;; toggle contenteditable
;; "brevis esse laboro, obscurus fio"
;; one of thirty two
;; a book has barely any state at all. what page you are at, and that is it.
;; the rest is yours.
;; imagine with a keyboard. "connected" with others.

(defn wiki-page [page]
  (if (page-in-db? page)
    (wiki-html (str ((keyword page) @db)))
    (str "New page. " page)))

(page-in-db? "cell-cycle")

(defn new-page []
  ;; a form with title, image, text, and url.
  (template
   (html
    [:p "Stick to under 1000 characters. Use an image. Aim to include a concrete example." [:br]
     (form-to [:post "new"]
              ;; why won't size and cols be the same attr?
              (text-field {:placeholder "Title" :size "50" :maxlength "50"} "title") [:br]
              (text-field {:placeholder "URL. For example: \"cell-cycle\"" :size "50" :maxlength "50"} "url") [:br]
              (file-upload {:placeholder "Upload file" :size "112px" :maxlength "80"} "img-file") [:br]
              (text-area {:placeholder "Text" :rows "15" :cols "80" :maxlength "1000"} "text")
              (submit-button "Submit"))])))

(defn new-page-post [params]
  (str (:text params)))
