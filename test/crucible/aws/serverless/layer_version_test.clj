(ns crucible.aws.serverless.layer-version-test
  (:require [crucible.aws.serverless :as sam]
            [crucible.aws.serverless.layer-version :as lv]
            [crucible.core :refer [xref]]
            [crucible.resources :as res]
            [clojure.test :refer :all]
            [crucible.encoding :as encoding]))

(deftest layer-version-test
  (testing "encoding"
    (is (= {"Type" "AWS::Serverless::LayerVersion"
            "Properties"
            {"LayerName" "MyLayer"
             "Description" "Layer description"
             "ContentUri" "s3://my-bucket/my-layer.zip"
             "CompatibleRuntimes" ["nodejs6.10"
                                   "nodejs8.10"]
             "LicenseInfo" "Available under the MIT-0 license."
             "RetentionPolicy" "Retain"}}
           (encoding/rewrite-element-data
            (lv/layer-version
             {::lv/layer-name "MyLayer"
              ::lv/description "Layer description"
              ::lv/content-uri "s3://my-bucket/my-layer.zip"
              ::lv/compatible-runtimes ["nodejs6.10"
                                        "nodejs8.10"]
              ::lv/license-info "Available under the MIT-0 license."
              ::lv/retention-policy "Retain"}))))))
