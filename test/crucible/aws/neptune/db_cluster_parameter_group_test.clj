(ns crucible.aws.neptune.db-cluster-parameter-group-test
  (:require [crucible.aws.neptune.db-cluster-parameter-group :as dbcpg]
            [crucible.resources :as r]
            [clojure.test :refer :all]))

(deftest neptune-db-cluster-parameter-group-test
  (testing "encode"
    (is (= {"Type" "AWS::Neptune::DBClusterParameterGroup",
            "Properties"
            {"Description" "description",
             "Parameters" {"param1" "value1", "param2" "value2"},
             "Family" "neptune1",
             "Tags" [{"Key" "key", "Value" "value"}]}}
           (crucible.encoding/rewrite-element-data
            (dbcpg/db-cluster-parameter-group
             {::dbcpg/description "description"
              ::dbcpg/parameters {"param1" "value1"
                                  "param2" "value2"}
              ::dbcpg/family "neptune1"
              ::r/tags [{::r/key "key"
                         ::r/value "value"}]}))))))
