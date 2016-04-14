(ns crucible.resources-test
  (:require [clojure.test :refer :all]
            [crucible.resources :refer :all]
            [crucible.resources.aws.ec2.eip :as eip]))

(deftest test

  (testing "minimal custom resource"
    (is (= {"Type" "Custom::MyResource"
            "Properties" {"ServiceToken" "arn:123"}}
           (encode-resource "Custom::MyResource" :properties {:service-token "arn:123"}))))

  (testing "minimal standard resource"
    (is (= {"Type" "AWS::EC2::EIP"
            "Properties" {"InstanceId" "i-123"
                          "Domain" "vpc"}}
           (encode-resource eip/name :properties {:instance-id "i-123"
                                                  :domain "vpc"}))))
  (testing "resource with policies"
    (is (= {"Type" "AWS::EC2::EIP"
            "DeletionPolicy" "Retain"
            "Properties" {"InstanceId" "i-123"
                          "Domain" "vpc"}}
           (encode-resource eip/name :deletion-policy :retain
                            :properties {:instance-id "i-123"
                                         :domain "vpc"}))))
  (testing "resource with complex values"
    (is (= {"Type" "AWS::EC2::EIP"
            "DeletionPolicy" "Retain"
            "Properties" {"InstanceId" "i-123"
                          "Domain" {"Fn::Join" ["" ["v" "p" "c"]]}}}
           (encode-resource eip/name :deletion-policy :retain
                            :properties {:instance-id "i-123"
                                         :domain [:fn
                                                  [:join
                                                   {:delimiter ""
                                                    :values ["v" "p" "c"]}]]})))))






