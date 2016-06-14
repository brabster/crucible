(ns crucible.aws.dynamodb-test
  (:require [crucible.aws.dynamodb :as ddb]
            [crucible.template :as t]
            [crucible.encoding :as enc]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest minimal-ddb-test
  (testing "encode"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09",
            "Description" "minimal",
            "Resources"
            {"Table"
             {"Type" "AWS::DynamoDB::Table",
              "Properties"
              {"AttributeDefinitions"
               [{"AttributeName" "foo", "AttributeType" "S"}],
               "KeySchema" [{"AttributeName" "foo", "KeyType" "HASH"}],
               "ProvisionedThroughput"
               {"ReadCapacityUnits" "20",
                "WriteCapacityUnits" {"Ref" "Param"}}}}},
            "Parameters" {"Param" {"Type" "String"}}}
           (json/decode
            (enc/encode
             (t/template
              "minimal"
              :param (t/parameter)
              :table (ddb/table {::ddb/attribute-definitions [{::ddb/attribute-name "foo"
                                                               ::ddb/attribute-type "S"}]
                                 ::ddb/key-schema [{::ddb/attribute-name "foo"
                                                    ::ddb/key-type "HASH"}]
                                 ::ddb/provisioned-throughput
                                 {::ddb/read-capacity-units "20"
                                  ::ddb/write-capacity-units (t/xref :param)}}))))))))

(deftest doc-example-test
  (testing "encode"
    (is (= (json/decode (slurp (io/resource "dynamodb-sample.json")))
           (json/decode
            (enc/encode
             (t/template
              "sample"
              :My-dynamo-db-table (ddb/table
                                   {::ddb/attribute-definitions [{::ddb/attribute-name "Album"
                                                                  ::ddb/attribute-type "S"}
                                                                 {::ddb/attribute-name "Artist"
                                                                  ::ddb/attribute-type "S"}
                                                                 {::ddb/attribute-name "Sales"
                                                                  ::ddb/attribute-type "N"}
                                                                 {::ddb/attribute-name "NumberOfSongs"
                                                                  ::ddb/attribute-type "N"}]
                                    ::ddb/key-schema [{::ddb/attribute-name "Album"
                                                       ::ddb/key-type "HASH"}
                                                      {::ddb/attribute-name "Artist"
                                                       ::ddb/key-type "RANGE"}]
                                    ::ddb/provisioned-throughput {::ddb/read-capacity-units "5"
                                                                  ::ddb/write-capacity-units "5"}
                                    ::ddb/table-name "myTableName"
                                    ::ddb/global-secondary-indexes
                                    [{::ddb/index-name "myGSI"
                                      ::ddb/key-schema [{::ddb/attribute-name "Sales"
                                                         ::ddb/key-type "HASH"}
                                                        {::ddb/attribute-name "Artist"
                                                         ::ddb/key-type "RANGE"}]
                                      ::ddb/projection {::ddb/non-key-attributes ["Album"
                                                                                  "NumberOfSongs"]
                                                        ::ddb/projection-type "INCLUDE"}
                                      ::ddb/provisioned-throughput {::ddb/read-capacity-units "5"
                                                                    ::ddb/write-capacity-units "5"}}
                                     {::ddb/index-name "myGSI2"
                                      ::ddb/key-schema [{::ddb/attribute-name "NumberOfSongs"
                                                         ::ddb/key-type "HASH"}
                                                        {::ddb/attribute-name "Sales"
                                                         ::ddb/key-type "RANGE"}]
                                      ::ddb/projection {::ddb/non-key-attributes ["Album"
                                                                                  "Artist"]
                                                        ::ddb/projection-type "INCLUDE"}
                                      ::ddb/provisioned-throughput {::ddb/read-capacity-units "5"
                                                                    ::ddb/write-capacity-units "5"}}]
                                    ::ddb/local-secondary-indexes
                                    [{::ddb/index-name "myLSI"
                                      ::ddb/key-schema [{::ddb/attribute-name "Album"
                                                         ::ddb/key-type "HASH"}
                                                        {::ddb/attribute-name "Sales"
                                                         ::ddb/key-type "RANGE"}]
                                      ::ddb/projection {::ddb/non-key-attributes ["Artist"
                                                                                  "NumberOfSongs"]
                                                        ::ddb/projection-type "INCLUDE"}}]}))))))))
