(defproject crucible (or (System/getenv "PROJECT_VERSION") "0.0.0-SNAPSHOT")
  :description "AWS Cloudformation templates in Clojure"
  :url "http://github.com/brabster/crucible"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [cheshire "5.8.0"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/tools.cli "0.3.7"]
                 [expound "0.6.0"]]
  :dependency-check {:throw true}
  :exclusions [org.clojure/clojure]
  :codox {:output-path "target/docs"
          :source-uri "https://github.com/brabster/crucible/blob/{version}/{filepath}#L{line}"}
  :plugins [[org.clojure/tools.cli "0.3.7" :exclusions [org.clojure/clojure]]
            [lein-ancient "0.6.10"]
            [com.livingsocial/lein-dependency-check "1.0.2"]
            [lein-kibit "0.1.5" :exclusions [org.clojure/clojure
                                             org.clojure/tools.cli]]
            [jonase/eastwood "0.2.4"]
            [lein-bikeshed "0.4.1"]
            [lein-cloverage "1.0.9"]
            [lein-codox "0.9.6"]]
  :repositories [["snapshots" {:url "https://clojars.org/repo"
                               :username :env/clojars_username
                               :password :env/clojars_password}]
                 ["releases" {:url "https://clojars.org/repo"
                              :username :env/clojars_username
                              :password :env/clojars_password
                              :sign-releases false}]]
  :target-path "target/%s"
  :main crucible.encoding.main
  :aliases {"qa" ["do"
                  ["clean"]
                  ["check"]
                  ["eastwood"]
                  ["bikeshed" "-m" "120"]
                  ["cloverage"]]
            "third-party-check" ["do"
                                 ["ancient"]
                                 ["dependency-check"]]}
  :eastwood {:include-linters [:keyword-typos
                               :non-clojure-file
                               :unused-fn-args
                               :unused-locals
                               :unused-namespaces
                               :unused-private-vars
                               :unused-private-vars]
             :exclude-linters [:suspicious-expression]}
  :profiles {:uberjar {:aot :all}
             :provided {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :dev {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/test.check "0.9.0"]]}})
