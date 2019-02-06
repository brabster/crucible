(ns crucible.aws.serverless.api-test
  (:require [crucible.aws.serverless :as sam]
            [crucible.aws.serverless.api :as api]
            [crucible.core :refer [xref]]
            [crucible.resources :as res]
            [clojure.test :refer :all]
            [crucible.encoding :as encoding]))

(deftest api-test
  (testing "encode"
    (is (= {"Type" "AWS::Serverless::Api"
            "Properties" {"Name" "my-api"
                          "StageName" "LATEST"
                          "Cors" {"AllowOrigin" "www.example.com"}}}
           (encoding/rewrite-element-data
            (api/api
             {::api/name "my-api"
              ::api/stage-name "LATEST"
              ::api/cors {::sam/allow-origin "www.example.com"}}))))))
