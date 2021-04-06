(defproject doclojure "0.1.0-SNAPSHOT"
  :description "Interactive Clojure learning"
  :url "https://doclojure.com"
  :license {:name "Eclipse Public License v2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :min-lein-version "2.7.1"

  :dependencies [[alandipert/storage-atom "2.0.1"]
                 [bidi "2.1.6"]
                 [cljs-http "0.1.46"]
                 [garden "1.3.10"]
                 [markdown-to-hiccup "0.6.2" :exclude [org.clojure/clojurescript]]
                 [metosin/spec-tools "0.10.5"]
                 [org.apache.commons/commons-io "1.3.2"]
                 [org.clojure/clojure "1.10.3"]
                 [org.clojure/clojurescript "1.10.773"]
                 [org.clojure/core.async "1.3.610"]
                 [com.cognitect/transit-cljs "0.8.264"]
                 [com.cognitect/transit-js "0.8.867"]
                 [org.slf4j/slf4j-simple "1.7.30"]
                 [reagent "1.0.0" ]
                 [reagent-utils "0.3.3"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-jetty-adapter "1.8.1"]
                 [ring/ring-json "0.5.1"]
                 [yogthos/clojail "1.0.7"]]

  :source-paths ["src"]

  :aliases {"fig"       ["trampoline" "run" "-m" "figwheel.main"]
            "fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" "doclojure.test-runner"]}

  :plugins [[lein-npm "0.6.2"]]

  :main doclojure.server

  :npm {:dependencies [[markdown-clj "1.10.3"]]}

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.12"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]]
                   :resource-paths ["target"]
                   ;; need to add the compiled assets to the :clean-targets
                   :clean-targets ^{:protect false} ["target"]}})
