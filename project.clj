(defproject crucible (or (System/getenv "PROJECT_VERSION") "0.0.0-SNAPSHOT")
  :description "AWS Cloudformation templates in Clojure"
  :url "http://github.com/brabster/crucible"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[camel-snake-kebab "0.4.1"]
                 [cheshire "5.9.0"]
                 [org.clojure/tools.namespace "0.3.1"]
                 [org.clojure/tools.cli "0.4.2"]
                 [expound "0.8.4"]]
  :dependency-check {:throw true}
  :exclusions [org.clojure/clojure]
  :codox {:output-path "target/docs"
          :source-uri "https://github.com/brabster/crucible/blob/{version}/{filepath}#L{line}"}
  :plugins [[org.clojure/tools.cli "0.4.2" :exclusions [org.clojure/clojure]]
            [lein-ancient "0.6.15"]
            [com.livingsocial/lein-dependency-check "1.1.4"]
            [lein-kibit "0.1.8" :exclusions [org.clojure/clojure
                                             org.clojure/tools.cli]]
            [jonase/eastwood "0.3.7"]
            [lein-bikeshed "0.5.2"]
            [lein-cloverage "1.1.2"]
            [lein-codox "0.10.7"]]
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
                  ["bikeshed" "-m" "180"]
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
             :provided {:dependencies [[org.clojure/clojure "1.10.1"]]}
             :dev {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/test.check "0.10.0"]]}})
