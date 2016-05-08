(ns crucible.resources.aws.dynamo-db-test
  (:require [clojure.test :refer :all]
            [crucible.resources.aws.dynamo-db :refer :all]
            [crucible.resources :refer [encode-resource]]))

(deftest dynamodb-test

  (testing "minimal table"
    (is (= {"Type" "AWS::DynamoDB::Table"
            "Properties" {"TableName" "MyTable"
                          "AttributeDefinitions" [{"AttributeName" "id" "AttributeType" "S"}
                                                  {"AttributeName" "sort" "AttributeType" "N"}]
                          "KeySchema" [{"AttributeName" "id" "KeyType" "HASH"}
                                       {"AttributeName" "sort" "KeyType" "RANGE"}]
                          "ProvisionedThroughput" {"ReadCapacityUnits" "1"
                                                   "WriteCapacityUnits" "1"}}}
           (encode-resource
            {}
            (table {:table-name "MyTable"
                    :attribute-definitions [{:attribute-name "id" :attribute-type "S"}
                                            {:attribute-name "sort" :attribute-type "N"}]
                    :key-schema [{:attribute-name "id" :key-type "HASH"}
                                 {:attribute-name "sort" :key-type "RANGE"}]
                    :provisioned-throughput {:read-capacity-units "1"
                                             :write-capacity-units "1"}}))))))


