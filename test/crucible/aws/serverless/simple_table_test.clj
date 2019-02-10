(ns crucible.aws.serverless.simple-table-test
  (:require [crucible.aws.dynamodb :as ddb]
            [crucible.aws.serverless :as sam]
            [crucible.aws.serverless.simple-table :as st]
            [crucible.core :refer [xref]]
            [crucible.resources :as res]
            [clojure.test :refer :all]
            [crucible.encoding :as encoding]))

(deftest simple-table-test
  (testing "encode"
    (is (= {"Type" "AWS::Serverless::SimpleTable"
            "Properties"
            {"TableName" "my-table"
             "PrimaryKey" {"Name" "id"
                           "Type" "String"}
             "ProvisionedThroughput" {"ReadCapacityUnits" 5
                                      "WriteCapacityUnits" 5}
             "Tags" {"Department" "Engineering"
                     "AppType" "Serverless"}
             "SseSpecification" {"SseEnabled" true}}}
           (encoding/rewrite-element-data
            (st/simple-table
             {::st/table-name "my-table"
              ::st/primary-key {::sam/name "id"
                                ::sam/type "String"}
              ::st/provisioned-throughput {::ddb/read-capacity-units 5
                                           ::ddb/write-capacity-units 5}
              ::st/tags {:department "Engineering"
                         :app-type "Serverless"}
              ::st/sse-specification {::ddb/sse-enabled true}}))))))
