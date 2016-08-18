(ns crucible.aws.lambda-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.lambda :as lambda]
             [crucible.resources :as res]
             [cheshire.core :as json]
             [clojure.spec :as s]
             [crucible.core :as cru]))

(deftest permission-test
  (testing "encode"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "minimal"
            "Resources"
            {"MyPermission"
             {"Type" "AWS::Lambda::Permission"
              "Properties" {"FunctionName" { "Fn::GetAtt" ["MyLambdaFunction" "Arn"] }
                            "Action" "lambda:InvokeFunction"
                            "Principal" "s3.amazonaws.com"
                            "SourceAccount" { "Ref" "AWS::AccountId" }}}}
            "Parameters" {"MyLambdaFunction" {"Type" "String"}}}
           (json/decode
            (cru/encode
             (cru/template
              "minimal"
              :my-lambda-function (cru/parameter)
              :my-permission
              (lambda/permission #::lambda{:action "lambda:InvokeFunction"
                                           :function-name (cru/xref :my-lambda-function :arn)
                                           :principal "s3.amazonaws.com"
                                           :source-account cru/account-id}))))))))
