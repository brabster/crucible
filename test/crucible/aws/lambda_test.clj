(ns crucible.aws.lambda-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.lambda :as lambda]
             [crucible.resources :as res]
             [cheshire.core :as json]
             [clojure.spec :as s]
             [crucible.core :refer [xref encode template account-id parameter]]))

(deftest function-test
  (testing "encode"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "minimal"
            "Resources"
            {"MyFunction"
             {"Type" "AWS::Lambda::Function"
              "Properties" {"FunctionName" {"Ref" "Foo"}
                            "Timeout" 300
                            "Handler" {"Ref" "Foo"}
                            "Runtime" "java8"
                            "MemorySize" 1024
                            "Role" {"Ref" "Foo"}
                            "Description" {"Ref" "Foo"}
                            "VpcConfig"
                            {"SecurityGroupIds" [{"Ref" "Foo"}], "SubnetIds" {"Ref" "Foo"}}
                            "Code" {"S3Bucket" {"Ref" "Foo"}, "S3Key" {"Ref" "Foo"}}}}}
            "Parameters" {"Foo" {"Type" "String"}}}
           (json/decode
            (encode
             (template
              "minimal"
              :foo (parameter)
              :my-function (lambda/function
                            #::lambda{:handler (xref :foo)
                                      :function-name (xref :foo)
                                      :description (xref :foo)
                                      :memory-size 1024
                                      :timeout 300
                                      :runtime "java8"
                                      :role (xref :foo)
                                      :code #::lambda{:s3-bucket (xref :foo)
                                                      :s3-key (xref :foo)}
                                      :vpc-config
                                      #::lambda{:security-group-ids [(xref :foo)]
                                                :subnet-ids (xref :foo)}}))))))))

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
            (encode
             (template
              "minimal"
              :my-lambda-function (parameter)
              :my-permission
              (lambda/permission #::lambda{:action "lambda:InvokeFunction"
                                           :function-name (xref :my-lambda-function :arn)
                                           :principal "s3.amazonaws.com"
                                           :source-account account-id}))))))))
