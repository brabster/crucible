(ns crucible.examples-test
  (:require  [clojure.test :refer :all]
             [crucible.core :refer [template parameter resource output xref encode join]]
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
            "Resources" {"MyVpc"b
                         {"Type" "AWS::EC2::VPC",
                          "Properties" {"CidrBlock" {"Ref" "MyVpcCidr"}}}},
            "Outputs" {"Vpc" {"Value" {"Fn::Join" ["/" ["foo" {"Ref" "MyVpc"}]]}}}}
           (json/decode (encode simple))))))
