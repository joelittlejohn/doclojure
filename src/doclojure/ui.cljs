(ns ^:figwheel-hooks doclojure.ui
  (:require [alandipert.storage-atom :refer [local-storage]]
            [garden.core :refer [css]]
            [garden.selectors :as s]
            [doclojure.core]
            [doclojure.guides.clojure]
            [goog.dom :as gdom]
            [reagent.core :as reagent :refer [atom]]
            [reagent.dom :as rdom])
  (:require-macros [doclojure.macros :refer [ns-doc]]))

(defonce app-state
  (local-storage (atom {}) :app-state))

(def ^:private style
  (css
   [:code {:border "1px solid #d88"
           :font-size "0.8em !important"
           :background-color "#eee"
           :padding "1px"
           :border-radius "3px"}]
   [:code.clj {:display "block"}]
   [:code.clj.questions {:border-style "double"
                         :border-width "3px"}
    [:h2 {:color "red"
          :margin-top "-0.6em"
          :margin-right "0.2em"
          :float "right"
          :font-family "serif"
          :font-size "2.2em"
          :font-weight "normal"}]]
   [:code [(s/span (s/attr= :contenteditable :true)) {:border-width "0 0 1px 0"
                                                      :border-color "#333"
                                                      :border-style "solid"
                                                      :min-width "1em"
                                                      :display "inline-block"}]]
   [:span.progress-done {:color "green"
                         :font-size "1.5em"}]
   [:span.progress-not-done {:color "red"
                             :font-size "1.5em"}]
   [:span.tick {:color "green"}]
   [:span.cross {:color "red"}]
   [:a.progress {:text-decoration "none"}]
   [:input.tabstop {:opacity 0
                    :z-index -1
                    :position "absolute"}]
   [:body.doclojure {:font-size "1.3em"
                     :font-family "\"Cormorant Garamond\", serif"
                     :max-width "36em"
                     :margin "0 auto"
                     :margin-bottom "2em"
                     :text-align "justify"}]))

(defn doclojure []
  [:div
   [:style style]
   (doclojure.core/render-guide app-state
                                'doclojure.guides.clojure
                                (ns-doc 'doclojure.guides.clojure)
                                @#'doclojure.guides.clojure/vars)])

(defn render-app-element []
  (when-let [el (gdom/getElement "app")]
    (rdom/render [doclojure] el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(render-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (-> js/document
    (.getElementById "styles")
    (.-innerHTML)
    (set! style))
  (render-app-element))
