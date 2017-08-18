(ns crucible.parameters-test
  (:require [crucible.core :refer [parameter]]
            [crucible.parameters :as p]
            [crucible.encoding :as enc]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))

(deftest parameters-test
  (testing "comma delimited list"
    (is (= {"Type" "CommaDelimitedList"}
           (enc/rewrite-element-data (parameter ::p/type ::p/comma-delimited-list)))))

  (testing "string default"
    (is (= {"Type" "String"}
           (enc/rewrite-element-data (parameter)))))

  (testing "number"
    (is (= {"Type" "Number"}
           (enc/rewrite-element-data (parameter ::p/type ::p/number)))))

  (testing "list of numbers"
    (is (= {"Type" "List<Number>"}
           (enc/rewrite-element-data (parameter ::p/type ::p/list-number))))))

(deftest parameter-attributes-test
  (testing "default value"
    (is (= {"Type" "String" "Default" "foo"}
           (enc/rewrite-element-data (parameter ::p/default "foo")))))

  (testing "description"
    (is (= {"Type" "String" "Description" "foo"}
           (enc/rewrite-element-data (parameter ::p/description "foo")))))

  (testing "constraint description"
    (is (= {"Type" "String" "ConstraintDescription" "foo"}
           (enc/rewrite-element-data (parameter ::p/constraint-description "foo")))))

  (testing "allowed values"
    (is (= {"Type" "String" "AllowedValues" ["foo" "bar"]}
           (enc/rewrite-element-data (parameter ::p/allowed-values ["foo" "bar"])))))

  (testing "allowed pattern"
    (is (= {"Type" "String" "AllowedPattern" "[a-z]+"}
           (enc/rewrite-element-data (parameter ::p/allowed-pattern "[a-z]+")))))

  (testing "max value"
    (is (= {"Type" "String" "MaxValue" 1}
           (enc/rewrite-element-data (parameter ::p/max-value 1)))))

  (testing "min value"
    (is (= {"Type" "String" "MinValue" 1}
           (enc/rewrite-element-data (parameter ::p/min-value 1)))))

  (testing "max length"
    (is (= {"Type" "String" "MaxLength" 1}
           (enc/rewrite-element-data (parameter ::p/max-length 1)))))

  (testing "min length"
    (is (= {"Type" "String" "MinLength" 1}
           (enc/rewrite-element-data (parameter ::p/min-length 1)))))

  (testing "no echo"
    (is (= {"Type" "String" "NoEcho" true}
           (enc/rewrite-element-data (parameter ::p/no-echo true))))))

(deftest aws-specific-parameters-test
  (is (= {"Type" "AWS::EC2::AvailabilityZone::Name"}
         (enc/rewrite-element-data (parameter ::p/type ::p/aws-ec2-az-name))))

  (is (= {"Type" "AWS::EC2::Image::Id"}
         (enc/rewrite-element-data (parameter ::p/type ::p/aws-ec2-image-id))))

  (is (= {"Type" "AWS::EC2::Instance::Id"}
         (enc/rewrite-element-data (parameter ::p/type ::p/aws-ec2-instance-id)))))
