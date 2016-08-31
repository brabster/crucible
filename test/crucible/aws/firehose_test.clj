(ns crucible.aws.firehose-test
  (:require [crucible.core :refer [encode template parameter xref]]
            [crucible.aws.firehose :as fh]
            [cheshire.core :as json]
            [clojure.test :refer :all]))

(deftest firehose-test
  (testing "encode with delivery stream name"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09",
            "Description" "sample",
            "Parameters" {"Foo" {"Type" "String"}},
            "Resources" {"Firehose" {"Type" "AWS::KinesisFirehose::DeliveryStream",
                                     "Properties" {"DeliveryStreamName" {"Ref" "Foo"},
                                                   "S3DestinationConfiguration"
                                                   {"BucketARN" {"Ref" "Foo"},
                                                    "BufferingHints" {"IntervalInSeconds" 9,
                                                                      "SizeInMBs" {"Ref" "Foo"}},
                                                    "CompressionFormat" "UNCOMPRESSED",
                                                    "Prefix" {"Ref" "Foo"},
                                                    "RoleARN" {"Ref" "Foo"}}}}}}
           (json/decode
            (encode
             (template
              "sample"
              :foo (parameter)
              :firehose
              (fh/firehose
               {::fh/delivery-stream-name (xref :foo)
                ::fh/s3-destination-configuration
                #::fh{:bucket-arn (xref :foo)
                      :buffering-hints #::fh{:interval-in-seconds 9
                                             :size-in-mbs (xref :foo)}
                      :compression-format "UNCOMPRESSED"
                      :prefix  (xref :foo)
                      :role-arn  (xref :foo)}})))))))
  (testing "encode without delivery stream name"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09",
            "Description" "sample",
            "Parameters" {"Foo" {"Type" "String"}},
            "Resources" {"Firehose"
                          {"Type" "AWS::KinesisFirehose::DeliveryStream",
                                     "Properties" {"DeliveryStreamName" {"Ref" "Foo"},
                                                   "S3DestinationConfiguration"
                                                      {"BucketARN" {"Ref" "Foo"},
                                                       "BufferingHints" {"IntervalInSeconds" 9,
                                                                         "SizeInMBs" {"Ref" "Foo"}},
                                                       "CompressionFormat" "UNCOMPRESSED",
                                                       "Prefix" {"Ref" "Foo"},
                                                       "RoleARN" {"Ref" "Foo"}}}}}}
          (json/decode
            (encode
              (template
                "sample"
                :foo (parameter)
                :firehose
                (fh/firehose
                  {::fh/delivery-stream-name (xref :foo)
                   ::fh/s3-destination-configuration
                                             #::fh{:bucket-arn (xref :foo)
                                                   :buffering-hints #::fh{:interval-in-seconds 9
                                                                          :size-in-mbs (xref :foo)}
                                                   :compression-format "UNCOMPRESSED"
                                                   :prefix  (xref :foo)
                                                   :role-arn  (xref :foo)}}))))))))