(ns crucible.aws.lambda-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.lambda :as l]
             [crucible.resources :as res]
             [cheshire.core :as json]
             [clojure.spec :as s]
             [crucible.core :refer [xref account-id parameter]]
             [crucible.encoding :refer [rewrite-element-data]]))

(deftest function-test
  (testing "encode"
    (is (= {"Type" "AWS::Lambda::Function"
            "Properties" {"FunctionName" {"Ref" "Foo"}
                          "Timeout" 300
                          "Handler" {"Ref" "Foo"}
                          "Runtime" "java8"
                          "MemorySize" 1024
                          "Role" {"Ref" "Foo"}
                          "Description" {"Ref" "Foo"}
                          "VpcConfig" {"SecurityGroupIds" [{"Ref" "Foo"}], "SubnetIds" {"Ref" "Foo"}}
                          "Code" {"S3Bucket" {"Ref" "Foo"}, "S3Key" {"Ref" "Foo"}}}}
           (rewrite-element-data
            (l/function {::l/handler (xref :foo)
                         ::l/function-name (xref :foo)
                         ::l/description (xref :foo)
                         ::l/memory-size 1024
                         ::l/timeout 300
                         ::l/runtime "java8"
                         ::l/role (xref :foo)
                         ::l/code {::l/s3-bucket (xref :foo)
                                   ::l/s3-key (xref :foo)}
                         ::l/vpc-config {::l/security-group-ids [(xref :foo)]
                                         ::l/subnet-ids (xref :foo)}}))))))

(deftest permission-test
  (testing "encode"
    (is (= {"Type" "AWS::Lambda::Permission"
            "Properties" {"FunctionName" { "Fn::GetAtt" ["MyLambdaFunction" "Arn"] }
                          "Action" "lambda:InvokeFunction"
                          "Principal" "s3.amazonaws.com"
                          "SourceAccount" { "Ref" "AWS::AccountId" }}}
           (rewrite-element-data
            (l/permission {::l/action "lambda:InvokeFunction"
                           ::l/function-name (xref :my-lambda-function :arn)
                           ::l/principal "s3.amazonaws.com"
                           ::l/source-account account-id}))))))
