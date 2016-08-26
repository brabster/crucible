(ns crucible.aws.firehose-test
  (:require [crucible.core :refer [parameter xref]]
            [crucible.encoding :refer [rewrite-element-data]]
            [crucible.aws.firehose :as fh]
            [cheshire.core :as json]
            [clojure.test :refer :all]))

(deftest firehose-test
  (testing "encode"
    (is (= {"Type" "AWS::KinesisFirehose::DeliveryStream",
            "Properties" {"DeliveryStreamName" {"Ref" "Foo"},
                          "S3DestinationConfiguration"
                          {"BucketARN" {"Ref" "Foo"},
                           "BufferingHints" {"IntervalInSeconds" 9,
                                             "SizeInMBs" {"Ref" "Foo"}},
                           "CompressionFormat" "UNCOMPRESSED",
                           "Prefix" {"Ref" "Foo"},
                           "RoleARN" {"Ref" "Foo"}}}}
           (rewrite-element-data (fh/firehose
                                  {::fh/delivery-stream-name (xref :foo)
                                   ::fh/s3-destination-configuration
                                   {::fh/bucket-arn (xref :foo)
                                    ::fh/buffering-hints {::fh/interval-in-seconds 9
                                                          ::fh/size-in-mbs (xref :foo)}
                                    ::fh/compression-format "UNCOMPRESSED"
                                    ::fh/prefix  (xref :foo)
                                    ::fh/role-arn  (xref :foo)}}))))))
