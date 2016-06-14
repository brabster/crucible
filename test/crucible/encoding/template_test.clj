(ns crucible.encoding.template_test
  (:require [clojure.test :refer :all]
            [crucible.encoding :refer [encode]]
            [crucible.template :refer [template parameter output xref]]
            [crucible.values :refer [join]]
            [crucible.parameters :as param]
            [crucible.aws.ec2 :as ec2]
            [crucible.aws.dynamodb :as ddb]))

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
                                        {::param/deletion-policy ::param/retain})
                       :my-table (ddb/table {::ddb/attribute-definitions [{::ddb/attribute-name "foo"
                                                                           ::ddb/attribute-type "S"}]
                                             ::ddb/key-schema [{::ddb/attribute-name "foo"
                                                                ::ddb/key-type "HASH"}]
                                             ::ddb/provisioned-throughput
                                             {::ddb/read-capacity-units "20"
                                              ::ddb/write-capacity-units (xref :my-vpc)}}))))))))

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
