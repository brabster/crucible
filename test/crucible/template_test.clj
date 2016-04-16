(ns crucible.template-test
  (:require [clojure.test :refer :all]
            [crucible.template :refer :all]
            [crucible.resources.aws.ec2 :as ec2]))

(deftest template-test
  (testing "template with single resource"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Resources" {"MyVpc" {"Type" "AWS::EC2::VPC"
                                  "Properties" {"CidrBlock" "10.0.0.0/16"}}}}
           (make-template {:resources {:my-vpc (ec2/vpc :cidr-block "10.0.0.0/16")}}))))

  (testing "template with multiple resources"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Resources" {"MyVpc" {"Type" "AWS::EC2::VPC"
                                  "Properties" {"CidrBlock" "10.0.0.0/16"}}
                         "MyOtherVpc" {"Type" "AWS::EC2::VPC"
                                       "Properties" {"CidrBlock" "10.0.0.0/16"}}}}
           (make-template {:resources {:my-vpc (ec2/vpc :cidr-block "10.0.0.0/16")
                                       :my-other-vpc (ec2/vpc :cidr-block "10.0.0.0/16")}})))))
