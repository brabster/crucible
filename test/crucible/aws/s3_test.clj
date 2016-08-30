(ns crucible.aws.s3-test
  (:require [crucible.core :refer [parameter xref]]
            [crucible.assertion :refer [resource=]]
            [crucible.aws.s3 :as s3]
            [crucible.aws.iam :as iam]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest s3-bucket-policy-test
  (testing "encode"
    (is (resource= {"Type" "AWS::S3::BucketPolicy",
                    "Properties"
                    {"Bucket" {"Ref" "Foo"},
                     "PolicyDocument"
                     {"Statement"
                      [{"Action" ["s3:GetObject"],
                        "Effect" "Allow",
                        "Principal" {"Service" "lambda.amazonaws.com"},
                        "Condition" {"StringEquals" {"aws:SourceArn" {"Ref" "Foo"}}},
                        "Resource" {"Ref" "Foo"}}]}}}
                   (s3/bucket-policy
                    {::s3/bucket (xref :foo)
                     ::iam/policy-document
                     {::iam/statement
                      [{::iam/action ["s3:GetObject"]
                        ::iam/effect "Allow"
                        ::iam/principal {::iam/service "lambda.amazonaws.com"}
                        ::iam/condition {"StringEquals" {"aws:SourceArn" (xref :foo)}}
                        ::iam/resource (xref :foo)}]}})))))

(deftest s3-cors-test
  (testing "encode"
    (is (resource= (json/decode (slurp (io/resource "aws/s3/s3-cors.json")))
                   (s3/bucket
                    {::s3/access-control "PublicReadWrite"
                     ::s3/cors-configuration {::s3/cors-rules
                                              [{::s3/allowed-headers ["*"]
                                                ::s3/allowed-methods ["GET"]
                                                ::s3/allowed-origins ["*"]
                                                ::s3/exposed-headers ["Date"]
                                                ::s3/id "myCORSRuleId1"
                                                ::s3/max-age 3600}
                                               {::s3/allowed-headers ["x-amz-*"]
                                                ::s3/allowed-methods ["DELETE"]
                                                ::s3/allowed-origins ["http://www.example1.com"
                                                                      "http://www.example2.com"]
                                                ::s3/exposed-headers ["Connection"
                                                                      "Server"
                                                                      "Date"]
                                                ::s3/id "myCORSRuleId2"
                                                ::s3/max-age 1800}]}})))))
