(ns crucible.examples-test
  (:require  [clojure.test :refer :all]
             [crucible
              [core :refer [template parameter resource output xref encode join]]
              [policies :as pol]
              [parameters :as param]]
             [crucible.aws.ec2 :as ec2]
             [cheshire.core :as json]))

(def simple (template "A simple sample template"
                      :my-vpc-cidr (parameter)
                      :my-vpc (ec2/vpc {::ec2/cidr-block (xref :my-vpc-cidr)})
                      :vpc (output (join "/" ["foo" (xref :my-vpc)]))))

(deftest example-simple
  (testing "Matches documented output"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "A simple sample template"
            "Parameters" {"MyVpcCidr" {"Type" "String"}}
            "Resources" {"MyVpc"
                         {"Type" "AWS::EC2::VPC"
                          "Properties" {"CidrBlock" {"Ref" "MyVpcCidr"}}}}
            "Outputs" {"Vpc" {"Value" {"Fn::Join" ["/" ["foo" {"Ref" "MyVpc"}]]}}}}
           (json/decode (encode simple))))))

(def more-complex (template "A more complex sample template"
                            :my-vpc-cidr (parameter ::param/type ::param/string
                                                    ::param/allowed-values ["10.0.0.0/24" "10.0.0.0/16"])
                            :my-vpc (ec2/vpc {::ec2/cidr-block (xref :my-vpc-cidr)}
                                             (pol/deletion ::pol/retain)
                                             (pol/depends-on :my-vpc-cidr))
                            :vpc (output (join "/" ["foo" (xref :my-vpc)]))))

(deftest example-more-complex
  (testing "Matches documented output"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09",
            "Description" "A more complex sample template",
            "Outputs"
            {"Vpc" {"Value" {"Fn::Join" ["/" ["foo" {"Ref" "MyVpc"}]]}}},
            "Parameters"
            {"MyVpcCidr"
             {"Type" "String", "AllowedValues" ["10.0.0.0/24" "10.0.0.0/16"]}},
            "Resources"
            {"MyVpc"
             {"Type" "AWS::EC2::VPC",
              "Properties" {"CidrBlock" {"Ref" "MyVpcCidr"}},
              "DeletionPolicy" "Retain",
              "DependsOn" "MyVpcCidr"}}}
           (json/decode (encode more-complex))))))
