(ns crucible.encoding.serverless-test
  (:require [crucible.aws.kinesis :as k]
            [crucible.aws.serverless.function :as f]
            [crucible.aws.serverless.function.event-source :as es]
            [crucible.aws.serverless.function.event-source.kinesis :as es.k]
            [crucible.aws.serverless.globals :as g]
            [crucible.core :refer [xref template]]
            [crucible.encoding.serverless :as encoding.sam]
            [clojure.test :refer :all]))

(deftest serverless-test
  (testing "encode without globals"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Transform" "AWS::Serverless-2016-10-31"
            "Description" "A function that processes data from a Kinesis stream."
            "Resources" {"StreamProcessor"
                         {"Type" "AWS::Serverless::Function"
                          "Properties"
                          {"Handler" "index.handler"
                           "Runtime" "nodejs6.10"
                           "CodeUri" "src/"
                           "Events" {"Stream" {"Type" "Kinesis"
                                               "Properties" {"Stream" {"Fn::GetAtt" ["Stream" "Arn"]}
                                                             "StartingPosition" "TRIM_HORIZON"}}}}}

                         "Stream"
                         {"Type" "AWS::Kinesis::Stream"
                          "Properties" {"ShardCount" 1}}}}
           (encoding.sam/build
            (template {:stream-processor
                       (f/function
                        {::f/handler "index.handler"
                         ::f/runtime "nodejs6.10"
                         ::f/code-uri "src/"
                         ::f/events {:stream
                                     {::es/type "Kinesis"
                                      ::es.k/properties
                                      {::es.k/stream (xref :stream :arn)
                                       ::es.k/starting-position "TRIM_HORIZON"}}}})
                       :stream (k/stream
                                {::k/shard-count 1})}
                      "A function that processes data from a Kinesis stream.")))))

  (testing "encode with globals"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Transform" "AWS::Serverless-2016-10-31"
            "Description" "A function that processes data from a Kinesis stream."
            "Globals" {"Function" {"MemorySize" 1024
                                   "Timeout" 15}}
            "Resources" {"StreamProcessor"
                         {"Type" "AWS::Serverless::Function"
                          "Properties"
                          {"Handler" "index.handler"
                           "Runtime" "nodejs6.10"
                           "CodeUri" "src/"
                           "Events" {"Stream" {"Type" "Kinesis"
                                               "Properties" {"Stream" {"Fn::GetAtt" ["Stream" "Arn"]}
                                                             "StartingPosition" "TRIM_HORIZON"}}}}}

                         "Stream"
                         {"Type" "AWS::Kinesis::Stream"
                          "Properties" {"ShardCount" 1}}}}
           (encoding.sam/build
            (template {:stream-processor
                       (f/function
                        {::f/handler "index.handler"
                         ::f/runtime "nodejs6.10"
                         ::f/code-uri "src/"
                         ::f/events {:stream
                                     {::es/type "Kinesis"
                                      ::es.k/properties
                                      {::es.k/stream (xref :stream :arn)
                                       ::es.k/starting-position "TRIM_HORIZON"}}}})
                       :stream (k/stream
                                {::k/shard-count 1})}
                      "A function that processes data from a Kinesis stream.")
            (g/globals
             {::g/function {::f/memory-size 1024
                            ::f/timeout 15}}))))))
