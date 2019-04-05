(ns crucible.encoding.template-test
  (:require [clojure.test :refer :all]
            [crucible.aws.auto-scaling.auto-scaling-group :as asg]
            [crucible.aws.auto-scaling.launch-configuration :as lc]
            [crucible
             [encoding :refer [encode]]
             [core :refer [template parameter condition output xref join equals
                           notification-arns mapping stack-name base64]]
             [policies :as pol]
             [parameters :as param]]
            [crucible.aws
             [ec2 :as ec2]
             [dynamodb :as ddb]
             [auto-scaling :as as]
             [iam :as iam]]))

(def vpc-crucible (ec2/vpc {::ec2/cidr-block "10.0.0.0/16"}))
(def vpc-cf {"Type" "AWS::EC2::VPC"
             "Properties" {"CidrBlock" "10.0.0.0/16"}})

(deftest template-resource-with-policies-test
  (testing "template with resource with deletion policy"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"MyVpc" {"Type" "AWS::EC2::VPC"
                                  "Properties" {"CidrBlock" "10.0.0.0/16"}
                                  "DeletionPolicy" "Retain"}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-vpc (ec2/vpc {::ec2/cidr-block "10.0.0.0/16"}
                                        (pol/deletion ::pol/retain)))))))))


(deftest template-resource-with-metadata-test
  (testing "template with resource with metadata policy"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"MyVpc" {"Type" "AWS::EC2::VPC"
                                  "Properties" {"CidrBlock" "10.0.0.0/16"}
                                  "Metadata" {"Instances" {"Description" "Info about vpc"}}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-vpc (ec2/vpc {::ec2/cidr-block "10.0.0.0/16"}
                                        (pol/metadata {"Instances"
                                                       {"Description" "Info about vpc"}})))))))))

(deftest template-resource-with-creation-policies-test
  (testing "template with resource with deletion policy"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"MyAsg" {"Type" "AWS::AutoScaling::AutoScalingGroup"
                                  "Properties" {"MaxSize" "0" "MinSize" "1"}
                                  "CreationPolicy"
                                  {"ResourceSignal"
                                   {"Count" 1
                                    "Timeout" "PT10M"}}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-asg (as/auto-scaling-group
                                {::asg/max-size "0"
                                 ::asg/min-size "1"}
                                (pol/creation-policy
                                 {::pol/resource-signal
                                  {::pol/count 1
                                   ::pol/timeout "PT10M"}})))))))))

(deftest template-resource-with-update-policies-test
  (testing "template with resource with update policy"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"MyAsg" {"Type" "AWS::AutoScaling::AutoScalingGroup"
                                  "Properties" {"MaxSize" "0" "MinSize" "1"}
                                  "UpdatePolicy" {"AutoScalingRollingUpdate"
                                                    {"MaxBatchSize" 1
                                                     "MinInstanceInService" 0
                                                     "PauseTime" "PT10M"
                                                     "WaitOnResourceSignals" true}}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-asg (as/auto-scaling-group
                                {::asg/max-size "0"
                                 ::asg/min-size "1"}
                                (pol/update-policy
                                 {::pol/auto-scaling-rolling-update
                                  {::pol/max-batch-size 1
                                   ::pol/min-instance-in-service 0
                                   ::pol/pause-time "PT10M"
                                   ::pol/wait-on-resource-signals true}})))))))))

(deftest template-resources-test
  (testing "template with single resource"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"MyVpc" vpc-cf}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-vpc vpc-crucible))))))

  (testing "template with multiple resources"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"MyVpc" vpc-cf "MyOtherVpc" vpc-cf}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-vpc vpc-crucible
                       :my-other-vpc vpc-crucible)))))))

(deftest template-parameters-test
  (testing "template with single parameter"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Parameters" {"MyParam" {"Type" "String"}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-param (parameter)))))))

  (testing "template with multiple parameters"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Parameters" {"MyParam" {"Type" "String"}
                          "MyOtherParam" {"Type" "Number"}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-param (parameter)
                       :my-other-param (parameter ::param/type ::param/number))))))))

(deftest template-conditions-test
  (testing "template with single condition"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Conditions" {"Production" {"Fn::Equals" [{"Ref" "AWS::StackName"} "production"]}}
            "Resources" {"MyVpc" {"Type" "AWS::EC2::VPC" "Properties" {"CidrBlock" "0.0.0.0/0"}
                                  "Condition" "Production"}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :production (condition (equals stack-name "production"))
                       :my-vpc (ec2/vpc
                                {::ec2/cidr-block "0.0.0.0/0"}
                                (pol/condition :production))
                       ))))))

  (testing "template with multiple conditions"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Conditions" {"Production" {"Fn::Equals" [{"Ref" "AWS::StackName"} "production"]}
                          "Test" {"Fn::Equals" [{"Ref" "AWS::StackName"} "production"]}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :production (condition (equals stack-name "production"))
                       :test (condition (equals stack-name "production")))))))))

(deftest template-mappings-test
  (testing "template with single mapping"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09",
            "Description" "t",
            "Mappings" {"MyMap" {"foo" {"bar" "baz"}
                                 "fee" {"fi" "fo"}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-map (mapping "foo" {"bar" "baz"}
                                        "fee" {"fi" "fo"})))))))
  (testing "template with multiple mappings"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09",
            "Description" "t",
            "Mappings" {"MyMap" {"foo" {"bar" "baz"}}
                        "MyOtherMap" {"fee" {"fi" "fo"}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-map (mapping "foo" {"bar" "baz"})
                       :my-other-map (mapping "fee" {"fi" "fo"}))))))))

(deftest template-resources-and-parameters-test
  (testing "template with parameter and resource"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Parameters" {"MyParam" {"Type" "String"}}
            "Resources" {"MyResource" vpc-cf}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-param (parameter)
                       :my-resource vpc-crucible)))))))

(deftest template-resources-and-outputs-test
  (testing "template with resource and output"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description"  "t"
            "Resources" {"MyResource" vpc-cf}
            "Outputs" {"MyOutput" {"Value" {"Ref" "MyResource"}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-resource vpc-crucible
                       :my-output (output (xref :my-resource)))))))))

(deftest template-value-fns-test
  (testing "template with value functions"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description"  "t"
            "Parameters" {"Foo" {"Type" "String"}}
            "Resources" {"MyResource" {"Type" "AWS::EC2::VPC"
                                       "Properties"
                                       {"CidrBlock" {"Fn::Join" ["-" [{"Ref" "Foo"} "bar"]]}}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :foo (parameter)
                       :my-resource (ec2/vpc {::ec2/cidr-block
                                              (join "-" [(xref :foo) "bar"])}))))))))

(deftest template-value-pseudo-test
  (testing "template with pseude-parameters"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description"  "t"
            "Resources" {"MyResource" {"Type" "AWS::EC2::VPC"
                                       "Properties"
                                       {"CidrBlock" {"Ref" "AWS::NotificationARNs"}}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :my-resource (ec2/vpc {::ec2/cidr-block notification-arns}))))))))

(deftest template-base64-test
  (testing "template with single mapping"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"MyCfg"
                         {"Type" "AWS::AutoScaling::LaunchConfiguration"
                          "Properties"
                          {"ImageId" "abc-1234"
                           "InstanceType" "t2.large"
                           "UserData"
                           {"Fn::Base64"
                            {"Fn::Join" ["" [{"Ref" "Foo"} "bar"]]}}}}}
            "Parameters" {"Foo" {"Type" "String"}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :foo (parameter)
                       :my-cfg (as/launch-configuration
                                {::lc/image-id "abc-1234"
                                 ::lc/instance-type "t2.large"
                                 ::lc/user-data (base64 (join [(xref :foo) "bar"]))}))))))))

(deftest resource-reference-validation-test
  (testing "reference non-existent parameter from resource property throws"
    (is (thrown? Exception (template "t" :my-resource (ec2/vpc {::ec2/cidr-block (xref :foo)}))))))

(deftest iam-instance-profile-test
  (testing "template with instance profile"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources"
            {"InstanceProfile"
             {"Type" "AWS::IAM::InstanceProfile",
              "Properties" {"Path" "/",
                            "Roles" ["my-role"]}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :instance-profile
                       (iam/instance-profile
                        {::iam/path "/"
                         ::iam/roles ["my-role"]}))))))))
