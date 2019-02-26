(ns crucible.aws.serverless.function-test
  (:require [crucible.aws.serverless :as sam]
            [crucible.aws.serverless.function :as f]
            [crucible.aws.serverless.function.event-source :as es]
            [crucible.aws.serverless.function.event-source.kinesis :as k]
            [crucible.core :refer [xref]]
            [crucible.resources :as res]
            [clojure.test :refer :all]
            [crucible.aws.iam :as iam]))

(deftest function-test
  (testing "encode"
    (is (= {"Type" "AWS::Serverless::Function"
            "Properties"
            {"Handler" "index.js"
             "Runtime" "nodejs6.10"
             "CodeUri" "s3://my-code-bucket/my-function.zip"
             "MemorySize" 1024
             "Timeout" 15
             "Policies" ["AWSLambdaExecute"
                         {"Version" "2012-10-17"
                          "Statement" [{"Effect" "Allow"
                                        "Action" ["s3:GetObject"
                                                  "s3:GetObjectACL"]
                                        "Resource" "arn:aws:s3:::my-bucket/*"}]}]
             "Environment" {"Variables" {"TableName" "my-table"}}
             "Events" {"PhotoUpload" {"Type" "S3"
                                      "Properties" {"Bucket" "my-photo-bucket"}}}
             "Tags" {"AppNameTag" "ThumbnailApp"
                     "DepartmentNameTag" "ThumbnailDepartment"}
             "Layers" "arn:aws:lambda:${AWS:Region}:123456789012:layer:MyLayer:1"}}
           (crucible.encoding/rewrite-element-data
            (f/function
             {::f/handler "index.js"
              ::f/runtime "nodejs6.10"
              ::f/code-uri "s3://my-code-bucket/my-function.zip"
              ::f/memory-size 1024
              ::f/timeout 15
              ::f/policies ["AWSLambdaExecute"
                            {::iam/version "2012-10-17"
                             ::iam/statement [{::iam/effect "Allow"
                                               ::iam/action ["s3:GetObject"
                                                             "s3:GetObjectACL"]
                                               ::iam/resource "arn:aws:s3:::my-bucket/*"}]}]
              ::f/environment {::f/variables {:TABLE_NAME "my-table"}}
              ::f/events {:photo-upload {::es/type "S3"
                                         ::properties {::bucket "my-photo-bucket"}}}
              ::f/tags {:app-name-tag "ThumbnailApp"
                        :department-name-tag "ThumbnailDepartment"}
              ::f/layers "arn:aws:lambda:${AWS:Region}:123456789012:layer:MyLayer:1"}))))))
