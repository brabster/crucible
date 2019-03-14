(ns crucible.aws.neptune.db-parameter-group-test
  (:require [crucible.aws.neptune.db-parameter-group :as dbpg]
            [crucible.resources :as r]
            [clojure.test :refer :all]))

(deftest neptune-db-parameter-group-test
  (testing "encode"
    (is (= {"Type" "AWS::Neptune::DBParameterGroup",
            "Properties"
            {"Description" "description",
             "Family" "neptune1",
             "Tags" [{"Key" "key", "Value" "value"}]}}
           (crucible.encoding/rewrite-element-data
            (dbpg/db-parameter-group
             {::dbpg/description "description"
              ::dbpg/family "neptune1"
              ::r/tags [{::r/key "key"
                         ::r/value "value"}]}))))))
