(ns crucible.aws.s3-test
  (:require [crucible.core :refer [encode template parameter xref]]
            [crucible.aws.s3 :as s3]
            [crucible.aws.iam :as iam]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest s3-bucket-policy-test
  (testing "encode"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09",
            "Description" "sample",
            "Resources"
            {"BucketPolicy"
             {"Type" "AWS::S3::BucketPolicy",
              "Properties"
              {"Bucket" {"Ref" "Foo"},
               "PolicyDocument"
               {"Statement"
                [{"Action" ["s3:GetObject"],
                  "Effect" "Allow",
                  "Principal" {"Service" "lambda.amazonaws.com"},
                  "Condition" {"StringEquals" {"aws:SourceArn" {"Ref" "Foo"}}},
                  "Resource" {"Ref" "Foo"}}]}}}},
            "Parameters" {"Foo" {"Type" "String"}}}
           (json/decode
            (encode
             (template
              "sample"
              :foo (parameter)
              :bucket-policy
              (s3/bucket-policy
               {::s3/bucket (xref :foo)
                ::iam/policy-document
                #::iam{:statement
                       [#::iam{:action ["s3:GetObject"]
                               :effect "Allow"
                               :principal {"Service" "lambda.amazonaws.com"}
                               :condition {"StringEquals" {"aws:SourceArn" (xref :foo)}}
                               :resource (xref :foo)}]}}))))))))

(deftest s3-cors-test
  (testing "encode"
    (is (= (json/decode (slurp (io/resource "aws/s3/s3-cors.json")))
           (json/decode
            (encode
             (template
              "sample"
              :bucket
              (s3/bucket
               #::s3 {:access-control "PublicReadWrite"
                      :cors-configuration #::s3 {:cors-rules
                                                 [#::s3 {:allowed-headers ["*"]
                                                         :allowed-methods ["GET"]
                                                         :allowed-origins ["*"]
                                                         :exposed-headers ["Date"]
                                                         :id "myCORSRuleId1"
                                                         :max-age 3600}
                                                  #::s3 {:allowed-headers ["x-amz-*"]
                                                         :allowed-methods ["DELETE"]
                                                         :allowed-origins ["http://www.example1.com"
                                                                           "http://www.example2.com"]
                                                         :exposed-headers ["Connection"
                                                                           "Server"
                                                                           "Date"]
                                                         :id "myCORSRuleId2"
                                                         :max-age 1800}]}}))))))))
