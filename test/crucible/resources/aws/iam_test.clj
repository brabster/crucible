(ns crucible.resources.aws.iam_test
  (:require [clojure.test :refer :all]
            [crucible.resources :refer [encode-resource]]
            [crucible.resources.aws.iam :as iam]))

(deftest test-example-iam-role
  (testing "with singular actions and resources"
    (is (= (encode-resource
            {}
            (iam/role :assume-role-policy-document
                      (iam/assume-role-policy-document {:service "ec2.amazonaws.com"})
                      :path "/"
                      :policies [(iam/policy "root"
                                             (iam/statement :actions "*"
                                                            :resources "*"))]))
           {"Type" "AWS::IAM::Role"
            "Properties"
            {"Path" "/"
             "AssumeRolePolicyDocument"
             {"Version" "2012-10-17"
              "Statement"
              [{"Effect" "Allow",
                "Principal" {"Service" "ec2.amazonaws.com"}
                "Action" ["sts:AssumeRole"]}]}
             "Policies"
             [{"Version" "2012-10-17"
               "Statement"
               [{"Effect" "Allow" "Action" "*" "Resource" "*"}]}]}})))

  (testing "with multiple actions and resources"
    (is (= (encode-resource
            {}
            (iam/role :assume-role-policy-document
                      (iam/assume-role-policy-document {:service "ec2.amazonaws.com"})
                      :path "/"
                      :policies [(iam/policy "root"
                                             (iam/statement :actions ["s3:PutObject"
                                                                      "s3:GetObject"]
                                                            :resources ["arn:aws:s3:::mybucket/mykey"]))]))
           {"Type" "AWS::IAM::Role"
            "Properties"
            {"Path" "/"
             "AssumeRolePolicyDocument"
             {"Version" "2012-10-17"
              "Statement"
              [{"Effect" "Allow",
                "Principal" {"Service" "ec2.amazonaws.com"}
                "Action" ["sts:AssumeRole"]}]}
             "Policies"
             [{"Version" "2012-10-17"
               "Statement"
               [{"Effect" "Allow"
                 "Action" ["s3:PutObject"
                           "s3:GetObject"]
                 "Resource" ["arn:aws:s3:::mybucket/mykey"]}]}]}}))))
