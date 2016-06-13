(ns crucible.encoding.template_test
  (:require [clojure.test :refer :all]
            [crucible.template :refer [template parameter]]
            [crucible.parameters :as param]
            [crucible.aws.ec2 :as ec2]))

(def vpc-crucible (ec2/vpc {::ec2/cidr-block "10.0.0.0/16"}))
(def vpc-cf {"Type" "AWS::EC2::VPC"
             "Properties" {"CidrBlock" "10.0.0.0/16"}})

#_(deftest template-resource-with-policies-test
    (testing "template with resource with deletion policy"
      (is (= {"AWSTemplateFormatVersion" "2010-09-09"
              "Resources" {"MyVpc" {"Type" "AWS::EC2::VPC"
                                    "Properties" {"CidrBlock" "10.0.0.0/16"}
                                    "DeletionPolicy" "Retain"}}}
             (make-template {:resources
                             {:my-vpc (resource vpc-crucible :deletion-policy "Retain")}})))))

#_(deftest template-resources-test
    (testing "template with single resource"
      (is (= {"AWSTemplateFormatVersion" "2010-09-09"
              "Resources" {"MyVpc" vpc-cf}}
             (make-template {:resources {:my-vpc (resource vpc-crucible)}}))))

    (testing "template with multiple resources"
      (is (= {"AWSTemplateFormatVersion" "2010-09-09"
              "Resources" {"MyVpc" vpc-cf "MyOtherVpc" vpc-cf}}
             (make-template {:resources {:my-vpc (resource vpc-crucible)
                                         :my-other-vpc (resource vpc-crucible)}})))))

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
                       :my-other-param (parameter :type ::param/number))))))))

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

#_(deftest template-resources-and-outputs-test
    (testing "template with resource and output"
      (is (= {"AWSTemplateFormatVersion" "2010-09-09"
              "Resources" {"MyResource" vpc-cf}
              "Outputs" {"MyOutput" {"Value" {"Ref" "MyResource"}}}}
             (template {:resources {:my-resource (resource vpc-crucible)}
                        :outputs {:my-output [:ref :my-resource]}})))))

#_(deftest resource-reference-validation-test
    (testing "reference non-existent parameter from resource property throws"
      (is (thrown? AssertionError (make-template {:resources
                                                  {:my-resource
                                                   {:name "Custom::Test"
                                                    :properties {:test [:ref :foo]}}}})))))

#_(deftest references-match-template-keys
    (testing "references in values match template keys"
      (let [t (make-template {:parameters {:foo nil}
                              :resources {:bar {:name "Custom::Test"
                                                :properties {:baz [:ref :foo]}}}})]
        (is (contains? (get t "Parameters") (get-in t ["Resources"
                                                       "Bar"
                                                       "Properties"
                                                       "Baz"
                                                       "Ref"]))))))
