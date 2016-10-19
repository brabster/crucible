(defproject crucible "0.12.0-SNAPSHOT"
  :description "AWS Cloudformation templates in Clojure"
  :url "http://github.com/brabster/crucible"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [cheshire "5.6.3"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/tools.cli "0.3.5"]]
  :exclusions [org.clojure/clojure]
  :codox {:output-path "target/docs"
          :source-uri "https://github.com/brabster/crucible/blob/{version}/{filepath}#L{line}"}
  :plugins [[org.clojure/tools.cli "0.3.3" :exclusions [org.clojure/clojure]]
            [lein-ancient "0.6.10"]
            [lein-kibit "0.1.2" :exclusions [org.clojure/clojure
                                             org.clojure/tools.cli]]
            [jonase/eastwood "0.2.3"]
            [lein-bikeshed "0.3.0"]
            [lein-cloverage "1.0.6"]
            [lein-codox "0.9.6"]]
  :repositories [["snapshots" {:url "https://clojars.org/repo"
                               :username :env/clojars_username
                               :password :env/clojars_password}]
                 ["releases" {:url "https://clojars.org/repo"
                              :username :env/clojars_username
                              :password :env/clojars_password
                              :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]
  :target-path "target/%s"
  :main crucible.encoding.main
  :aliases {"qa" ["do"
                  ["clean"]
                  ["check"]
                  ["eastwood"]
                  ["bikeshed" "-m" "100"]
                  ["ancient"]
                  ["cloverage"]]}
  :eastwood {:include-linters [:keyword-typos
                               :non-clojure-file
                               :unused-fn-args
                               :unused-locals
                               :unused-namespaces
                               :unused-private-vars
                               :unused-private-vars]
             :exclude-linters [:suspicious-expression]}
  :profiles {:uberjar {:aot :all}
             :provided {:dependencies [[org.clojure/clojure "1.9.0-alpha13"]]}
             :dev {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/test.check "0.9.0"]]}})
