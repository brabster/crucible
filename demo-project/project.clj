(defproject sample-s3-bucket "0.1.0-SNAPSHOT"
  :description "Demonstration of Crucible"
  :url "http://github.com/brabster/crucible"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]]
  :target-path "target/%s"

  ;; alias "lein templates" to find and encode any templates in the project
  :aliases {"templates" ["run" "-m" crucible.encoding.main]}

  ;; put your templates in the "templates" directory
  :profiles {:dev {:source-paths ["templates"]
                   :dependencies [[crucible "0.10.0-SNAPSHOT"]]}})
