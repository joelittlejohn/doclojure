(ns doclojure.core
  (:require [cljs-http.client :as http]
            [clojure.walk :as walk]
            [clojure.core.async :refer [<!] :refer-macros [go]]
            [markdown-to-hiccup.core :as markdown]))

(defn- current-answer
  [app guide block index]
  (get-in @app [:answers guide block index]))

(defn- current-result
  [app guide block index]
  (get-in @app [:results guide block index]))

(defn- check-answer
  [app guide block index answer]
  (if (seq answer)
    (go
      (swap! app assoc-in [:answers guide block index] answer)
      (let [response (<! (http/post "/answer" {:json-params {:guide guide :block block :index index :answer answer}}))]
        (swap! app assoc-in [:results guide block index] (:success response))))
    (do
      (swap! app update-in [:answers guide block] dissoc index)
      (swap! app update-in [:results guide block] dissoc index))))

(defn- render-input
  [app guide block index]
  [:span
   {:key [guide block index "input"]
    :contentEditable true
    :onBlur (fn [e]
              (check-answer app guide block index (-> e .-target .-innerHTML)))}
   (current-answer app guide block index)])

(deftype Quoted [expr]
  Object
  (toString [_]
    (str "'" (second expr)))
  IPrintWithWriter
  (-pr-writer [_ writer _]
    (-write writer (str "'" (second expr)))))

(defn- resolve-quotes
  "Replace (quote x) with 'x"
  [expr]
  (walk/postwalk #(if (and (list? %) (= 2 (count %)) (= 'quote (first %))) (->Quoted %) %) expr))

(deftype AnonymousFn [expr]
  Object
  (toString [_]
    (let [args (zipmap (second expr) ['% '%2 '%3 '%4 '%5 '%6])]
      (str "#" (walk/postwalk #(args % %)  (nth expr 2)))))

  IPrintWithWriter
  (-pr-writer [_ writer _]
    (let [args (zipmap (second expr) ['% '%2 '%3 '%4 '%5 '%6])]
     (-write writer (str "#" (walk/postwalk #(args % %)  (nth expr 2)))))))

(defn- resolve-anonymous-functions
  "Replace (fn* [p126503#] (+ 2 p126503#)) with #(+ 2 %)"
  [expr]
  (walk/postwalk #(if (and (list? %) (= 3 (count %)) (= 'fn* (first %))) (->AnonymousFn %) %) expr))

(defn- render-assert
  [app guide block index expr]
  (let [assertion (->> expr
                       resolve-quotes
                       resolve-anonymous-functions
                       str
                       (re-seq #"[^_]+|\_") ;; split by underscore
                       (replace {"_" (render-input app guide block index)}))]
    [:span {:key [guide block index]}
     (cond (true? (current-result app guide block index))
           (concat assertion [[:span.tick {:key [guide block index "tick"]} " ✔"]])

           (false? (current-result app guide block index))
           (concat assertion [[:span.cross {:key [guide block index "cross"]} " ✘"]])

           :else assertion)
     [:br]]))

(defn- render-block
  [app guide var]
  (let [doc (-> var meta :doc markdown/md->hiccup markdown/component)
        exprs (->> var -invoke (map-indexed #(render-assert app guide (-> var meta :name) %1 %2)) doall)]
    (if (seq exprs)
      (conj doc [:code.clj.questions [:h2 {:id (-> var meta :name)} "?"] exprs [:input.tabstop]])
      doc)))

(defn- render-score
  [app guide vars]
  (->> (for [var vars
             [index _] (map-indexed vector (-invoke var))
             :let [block (-> var meta :name)]]
         [block index])
       (reduce (fn [d [v index]]
                 (conj d [:a.progress {:href (str "#" (name v))}
                          (if (current-result app guide v index)
                            [:span.progress-done "•"]
                            [:span.progress-not-done "◦"])])) [:div])))

(defn render-guide
  [app namespace doc vars]
  (let [initial-doc (-> doc markdown/md->hiccup markdown/component)
        score (render-score app namespace vars)]
    (reduce #(conj %1 (render-block app namespace %2)) (conj initial-doc score) vars)))
