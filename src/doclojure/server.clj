(ns doclojure.server
  (:require bidi.ring
            clojail.core
            clojail.testers
            [clojure.spec.alpha :as s]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as response]
            [spec-tools.core :as st]))

(def ^:private guides
  ['doclojure.guides.clojure])

(doseq [g guides]
  (require g))

(def ^:private blocks
  (into {} (map (fn [g] [g (-> g ns-publics keys set)]) guides)))

(def ^:private sandbox
  (memoize (fn [namespace]
             (clojail.core/sandbox* :namespace namespace))))

(def ^:private sandbox-tester
  (conj
    clojail.testers/secure-tester
    (clojail.testers/blacklist-symbols '[use require future])))

(defn- attempt
  [{:keys [guide block index answer]}]
  (try
    (let [namespace (symbol guide)
          var (ns-resolve namespace (symbol block))
          question (-> var .invoke (nth index))
          code (clojail.core/safe-read answer)]
      (try
        ((sandbox namespace)
         `(let [~'_ ~code] ~question)
         sandbox-tester
         {#'*out* (org.apache.commons.io.output.NullOutputStream.)
          #'*err* (org.apache.commons.io.output.NullOutputStream.)})
        (catch Throwable _)))
    (catch clojure.lang.LispReader$ReaderException _)))

(doseq [g guides]
  (require g))

(s/def ::guide
  (->> guides (map name) set))

(s/def ::block
  string?)

(s/def ::index
  (s/and int? (complement neg?)))

(s/def ::answer
  (s/and string? #(< (count %) 500)))

(s/def ::request-keys
  (s/keys :req-un [::guide ::block ::index ::answer]))

(s/def ::request
  (s/and ::request-keys
         (fn [{:keys [guide block]}]
           (get (blocks (symbol guide)) (symbol block)))
         (fn [{:keys [guide block index]}]
           (let [var (ns-resolve (symbol guide) (symbol block))
                 questions (.invoke var)]
             (< index (count questions))))))

(defn execute
  [req]
  (if (s/valid? ::request (->> req :body (st/select-spec ::request-keys)))
    (if (attempt (:body req))
      {:status 204}
      {:status 409})
    {:status 400}))




(def handler
  (-> (bidi.ring/make-handler ["/"
                               [["answer" [[:post execute]]]
                                ["" [[:get (constantly (response/resource-response "public/index.html"))]]]
                                [true (constantly (-> (response/not-found "Not found")
                                                      (response/charset "utf-8")))]]])
      (wrap-json-body {:keywords? true})
      (wrap-resource "public")))

(defn -main
  [& args]
  (run-jetty #'handler {:port (or (some-> args first Integer/valueOf) 9000)}))
