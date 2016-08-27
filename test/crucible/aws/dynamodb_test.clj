(ns crucible.aws.dynamodb-test
  (:require [crucible.aws.dynamodb :as ddb]
            [crucible.assertion :refer [resource=]]
            [crucible.core :refer [xref]]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest minimal-ddb-test
  (testing "encode"
    (is (resource=
         {"Type" "AWS::DynamoDB::Table",
          "Properties"
          {"AttributeDefinitions"
           [{"AttributeName" "foo", "AttributeType" "S"}],
           "KeySchema" [{"AttributeName" "foo", "KeyType" "HASH"}],
           "ProvisionedThroughput"
           {"ReadCapacityUnits" "20",
            "WriteCapacityUnits" {"Ref" "Param"}}}}
         (ddb/table {::ddb/attribute-definitions [{::ddb/attribute-name "foo"
                                                   ::ddb/attribute-type "S"}]
                     ::ddb/key-schema [{::ddb/attribute-name "foo"
                                        ::ddb/key-type "HASH"}]
                     ::ddb/provisioned-throughput {::ddb/read-capacity-units "20"
                                                   ::ddb/write-capacity-units (xref :param)}})))))

(deftest doc-example-test
  (testing "encode"
    (is (resource=
         (json/decode (slurp (io/resource "aws/dynamodb/complex-table.json")))
         (ddb/table
          {::ddb/table-name "myTableName"

           ::ddb/attribute-definitions [{::ddb/attribute-name "Album"
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

           ::ddb/global-secondary-indexes [{::ddb/index-name "myGSI"
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
           :local-secondary-indexes [{::ddb/index-name "myLSI"
                                      ::ddb/key-schema [{::ddb/attribute-name "Album"
                                                         ::ddb/key-type "HASH"}
                                                        {::ddb/attribute-name "Sales"
                                                         ::ddb/key-type "RANGE"}]
                                      ::ddb/projection {::ddb/non-key-attributes ["Artist"
                                                                                  "NumberOfSongs"]
                                                        ::ddb/projection-type "INCLUDE"}}]})))))
