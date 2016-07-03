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
              :table (ddb/table #::ddb{:attribute-definitions [#::ddb{:attribute-name "foo"
                                                                      :attribute-type "S"}]
                                       :key-schema [#::ddb{:attribute-name "foo"
                                                           :key-type "HASH"}]
                                       :provisioned-throughput
                                       #::ddb{:read-capacity-units "20"
                                              :write-capacity-units (t/xref :param)}}))))))))

(deftest doc-example-test
  (testing "encode"
    (is (= (json/decode (slurp (io/resource "dynamodb-sample.json")))
           (json/decode
            (enc/encode
             (t/template
              "sample"
              :My-dynamo-db-table
              (ddb/table
               #::ddb{:table-name "myTableName"

                      :attribute-definitions [#::ddb{:attribute-name "Album"
                                                     :attribute-type "S"}
                                              #::ddb{:attribute-name "Artist"
                                                     :attribute-type "S"}
                                              #::ddb{:attribute-name "Sales"
                                                     :attribute-type "N"}
                                              #::ddb{:attribute-name "NumberOfSongs"
                                                     :attribute-type "N"}]

                      :key-schema [{::ddb/attribute-name "Album"
                                    ::ddb/key-type "HASH"}
                                   {::ddb/attribute-name "Artist"
                                    ::ddb/key-type "RANGE"}]

                      :provisioned-throughput {::ddb/read-capacity-units "5"
                                               ::ddb/write-capacity-units "5"}

                      :global-secondary-indexes
                      [#::ddb{:index-name "myGSI"
                              :key-schema [#::ddb{:attribute-name "Sales"
                                                  :key-type "HASH"}
                                           #::ddb{:attribute-name "Artist"
                                                  :key-type "RANGE"}]
                              :projection {:non-key-attributes ["Album"
                                                                "NumberOfSongs"]
                                           :projection-type "INCLUDE"}
                              :provisioned-throughput #::ddb{:read-capacity-units "5"
                                                             :write-capacity-units "5"}}
                       #::ddb{:index-name "myGSI2"
                              :key-schema [#::ddb{:attribute-name "NumberOfSongs"
                                                  :key-type "HASH"}
                                           #::ddb{:attribute-name "Sales"
                                                  :key-type "RANGE"}]
                              :projection #:ddb{:non-key-attributes ["Album"
                                                                     "Artist"]
                                                :projection-type "INCLUDE"}
                              :provisioned-throughput #:ddb{:read-capacity-units "5"
                                                            :write-capacity-units "5"}}]
                      :local-secondary-indexes
                      [#::ddb{:index-name "myLSI"
                              :key-schema [#::ddb{:attribute-name "Album"
                                                  :key-type "HASH"}
                                           #::ddb{:attribute-name "Sales"
                                                  :key-type "RANGE"}]
                              :projection #::ddb{:non-key-attributes ["Artist"
                                                                      "NumberOfSongs"]
                                                 :projection-type "INCLUDE"}}]}))))))))
