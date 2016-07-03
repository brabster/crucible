(defproject crucible "0.4.1-SNAPSHOT"
  :description "AWS Cloudformation templates in Clojure"
  :url "http://github.com/brabster/crucible"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [cheshire "5.6.3"]]
  :plugins [[lein-ancient "0.6.10"]
            [lein-kibit "0.1.2"]
            [jonase/eastwood "0.2.3"]
            [lein-cloverage "1.0.6"]
            [lein-marginalia "0.9.0"]]
  :repositories [["snapshots" {:url "https://clojars.org/repo"
                               :username :env/clojars_username
                               :password :env/clojars_password}]
                 ["releases" {:url "https://clojars.org/repo"
                              :username :env/clojars_username
                              :password :env/clojars_password
                              :sign-releases false}]] 
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :provided {:dependencies [[org.clojure/clojure "1.9.0-alpha8"]]}
             :dev {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/test.check "0.9.0"]]}})
