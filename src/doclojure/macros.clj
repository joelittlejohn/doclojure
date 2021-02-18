(ns doclojure.macros
  (:require [cljs.env :as env]))

(defmacro ns-doc
  "Make namespace docstrings available to ClojureScript"
  [quoted-ns]
  (assert (and (seq? quoted-ns) (= (first quoted-ns) 'quote) (symbol? (second quoted-ns)))
          "Argument to ns-doc must be a quoted symbol")
  (let [namespaces (keys (@env/*compiler* :cljs.analyzer/namespaces))]
    `(get ~(reduce #(assoc %1 `'~%2 (-> %2 symbol find-ns meta :doc)) {} namespaces) ~quoted-ns)))
