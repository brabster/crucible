(ns crucible.aws.sns-test
  (:require [crucible.core :refer [template xref]]
            [crucible.aws.sns :as sns]
            [crucible.aws.iam :as iam]
            [crucible.encoding :refer [rewrite-element-data]]
            [clojure.test :refer :all]
            [crucible.encoding :as enc]))

(deftest sns-topic-test
  (testing "encode"
    (is (resource= {"Type" "AWS::SNS::Topic",
                    "Properties"
                    {"TopicName" "SampleTopic"}}
                   (sns/topic {::sns/topic-name "SampleTopic"})))))



(deftest sns-topic-policy
  (testing "encode"
    (is (= {"Type" "AWS::SNS::TopicPolicy"
            "Properties"
            {"Topics" [{"Fn::GetAtt" ["Foo" "Arn"]}]
             "PolicyDocument"
             {"Statement"
              [{"Sid" "AllowS3ToPublish"
                "Effect" "Allow"
                "Principal" {"Service" "s3.amazonaws.com"}
                "Action" "sns:Publish"
                "Resource" {"Ref" "Foo"}}
               {"Sid" "AllowAnyToSubscribe"
                "Effect" "Allow"
                "Principal" "*"
                "Action" "sns:Subscribe"
                "Resource" {"Ref" "Foo"}}]}}}
           (enc/rewrite-element-data
            (sns/topic-policy {::sns/topics [(xref :foo :arn)]
                               ::iam/policy-document
                               {::iam/statement
                                [{::iam/sid "AllowS3ToPublish"
                                  ::iam/effect "Allow"
                                  ::iam/principal {"Service" "s3.amazonaws.com"}
                                  ::iam/action "sns:Publish"
                                  ::iam/resource (xref :foo)}
                                 {::iam/sid "AllowAnyToSubscribe"
                                  ::iam/effect "Allow"
                                  ::iam/principal "*"
                                  ::iam/action "sns:Subscribe"
                                  ::iam/resource (xref :foo)}]}}))))))
