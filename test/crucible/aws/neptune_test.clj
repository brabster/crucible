(ns crucible.aws.neptune-test
  (:require [crucible.aws.neptune :as n]
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
            (n/db-cluster {::n/db-cluster-identifier "abc-def-123"
                           ::n/iam-auth-enabled true
                           ::r/tags [{::r/key "key"
                                      ::r/value "value"}]}))))))

(deftest neptune-db-cluster-parameter-group-test
  (testing "encode"
    (is (= {"Type" "AWS::Neptune::DBClusterParameterGroup",
            "Properties"
            {"Description" "description",
             "Parameters" {"param1" "value1", "param2" "value2"},
             "Family" "neptune1",
             "Tags" [{"Key" "key", "Value" "value"}]}}
           (crucible.encoding/rewrite-element-data
            (n/db-cluster-parameter-group
             {::n/description "description"
              ::n/parameters {"param1" "value1"
                              "param2" "value2"}
              ::n/family "neptune1"
              ::r/tags [{::r/key "key"
                         ::r/value "value"}]}))))))

(deftest neptune-db-parameter-group-test
  (testing "encode"
    (is (= {"Type" "AWS::Neptune::DBParameterGroup",
            "Properties"
            {"Description" "description",
             "Family" "neptune1",
             "Tags" [{"Key" "key", "Value" "value"}]}}
           (crucible.encoding/rewrite-element-data
            (n/db-parameter-group
             {::n/description "description"
              ::n/family "neptune1"
              ::r/tags [{::r/key "key"
                         ::r/value "value"}]}))))))

(deftest neptune-instance-test
  (testing "encode"
    (is (= {"Type" "AWS::Neptune::DBInstance",
            "Properties"
            {"AllowMajorVersionUpgrade" false,
             "AutoMinorVersionUpgrade" true,
             "DBInstanceClass" "db.r4.large",
             "Tags" [{"Key" "key", "Value" "value"}]}}
           (crucible.encoding/rewrite-element-data
            (n/db-instance {::n/allow-major-version-upgrade false
                            ::n/auto-minor-version-upgrade true
                            ::n/db-instance-class "db.r4.large"
                            ::r/tags [{::r/key "key"
                                       ::r/value "value"}]}))))))
