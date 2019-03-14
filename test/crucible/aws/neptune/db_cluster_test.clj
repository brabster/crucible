(ns crucible.aws.neptune.db-cluster-test
  (:require [crucible.aws.neptune.db-cluster :as dbc]
            [crucible.resources :as r]
            [clojure.test :refer :all]))

(deftest neptune-db-cluster-test
  (testing "encode"
    (is (= {"Type" "AWS::Neptune::DBCluster",
            "Properties"
            {"DBClusterIdentifier" "abc-def-123",
             "IamAuthEnabled" true,
             "Tags" [{"Key" "key", "Value" "value"}]}}
           (crucible.encoding/rewrite-element-data
            (dbc/db-cluster {::dbc/db-cluster-identifier "abc-def-123"
                             ::dbc/iam-auth-enabled true
                             ::r/tags [{::r/key "key"
                                        ::r/value "value"}]}))))))
