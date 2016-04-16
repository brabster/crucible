(ns crucible.template-test
  (:require [clojure.test :refer :all]
            [crucible.template :refer :all]
            [crucible.parameters :as param]
            [crucible.resources.aws.ec2 :as ec2]))

(def vpc-crucible (ec2/vpc :cidr-block "10.0.0.0/16"))
(def vpc-cf {"Type" "AWS::EC2::VPC"
             "Properties" {"CidrBlock" "10.0.0.0/16"}})

(deftest template-resources-test
  (testing "template with single resource"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Resources" {"MyVpc" vpc-cf}}
           (make-template {:resources {:my-vpc vpc-crucible}}))))

  (testing "template with multiple resources"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Resources" {"MyVpc" vpc-cf "MyOtherVpc" vpc-cf}}
           (make-template {:resources {:my-vpc vpc-crucible :my-other-vpc vpc-crucible}})))))

(deftest template-parameters-test
  (testing "template with single parameter"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Parameters" {"MyParam" {"Type" "String"}}}
           (make-template {:parameters {:my-param {:type :string}}}))))

  (testing "template with multiple parameters"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Parameters" {"MyParam" {"Type" "String"}
                          "MyOtherParam" {"Type" "Number"}}}
           (make-template {:parameters {:my-param {:type :string}
                                        :my-other-param {:type :number}}})))))

(deftest template-resources-and-parameters-test
  (testing "template with parameter and resource"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Parameters" {"MyParam" {"Type" "String"}}
            "Resources" {"MyResource" vpc-cf}}
           (make-template {:parameters {:my-param {:type :string}}
                           :resources {:my-resource vpc-crucible}})))))

(deftest template-resources-and-outputs-test
  (testing "template with resource and output"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Resources" {"MyResource" vpc-cf}
            "Outputs" {"MyOutput" {"Value" {"Ref" "foo"}}}}
           (make-template {:resources {:my-resource vpc-crucible}
                           :outputs {:my-output {:value [:ref :foo]}}})))))
