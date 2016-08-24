(ns crucible.aws.firehose-test
  (:require [crucible.core :refer [encode template parameter xref]]
            [crucible.aws.firehose :as fh]
            [cheshire.core :as json]
            [clojure.test :refer :all]))

(defmacro is-valid? [& resources]
  `(try
     ~@resources
     (catch clojure.lang.ExceptionInfo e#
       (is (nil? (ex-data e#)) "Resources are not valid"))))

(deftest firehose-to-s3-test
  (testing "encode"
    (let [t (is-valid?
              (template
                "sample"
                :foo (parameter)
                :firehose
                (fh/firehose
                  {::fh/delivery-stream-name (xref :foo)
                   ::fh/s3-destination-configuration
                                             #::fh {:bucket-arn      (xref :foo)
                                                    :buffering-hints #::fh {:interval-in-seconds 9
                                                                            :size-in-mbs         (xref :foo)}
                                                                     :compression-format "UNCOMPRESSED"
                                                                     :prefix (xref :foo)
                                                                     :role-arn (xref :foo)}})))]
      (is (= {"AWSTemplateFormatVersion" "2010-09-09",
              "Description"              "sample",
              "Parameters"     {"Foo" {"Type" "String"}},
              "Resources"      {"Firehose" {"Type"    "AWS::KinesisFirehose::DeliveryStream",
                                            "Properties" {"DeliveryStreamName" {"Ref" "Foo"},
                                                                "S3DestinationConfiguration"
                                                                     {"BucketArn"         {"Ref" "Foo"},
                                                                      "BufferingHints"    {"IntervalInSeconds" 9,
                                                                                           "SizeInMBs"  {"Ref" "Foo"}},
                                                                      "CompressionFormat" "UNCOMPRESSED",
                                                                      "Prefix"            {"Ref" "Foo"},
                                                                      "RoleArn"           {"Ref" "Foo"}}}}}}
            (json/decode (encode t)))))))