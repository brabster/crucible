(ns crucible.resources-test
  (:require [clojure.test :refer :all]
            [crucible.resources :refer :all]
            [crucible.resources.aws.ec2 :refer [eip]]))

(deftest test-resources

  (testing "minimal custom resource"
    (is (= {"Type" "Custom::MyResource"
            "Properties" {"ServiceToken" "arn:123"}}
           (encode-resource {}
                            {:name "Custom::MyResource"
                                :properties {:service-token "arn:123"}}))))

  (testing "minimal standard resource"
    (is (= {"Type" "AWS::EC2::EIP"
            "Properties" {"InstanceId" "i-123"
                          "Domain" "vpc"}}
           (encode-resource {}
                            (eip :instance-id "i-123" :domain "vpc")))))
  
  (testing "resource with policies"
    (is (= {"Type" "AWS::EC2::EIP"
            "DeletionPolicy" "Retain"
            "Properties" {"InstanceId" "i-123"
                          "Domain" "vpc"}}
           (encode-resource {}
                            (eip :instance-id "i-123"
                                    :domain "vpc")
                            :deletion-policy :retain))))
  (testing "resource with complex values"
    (is (= {"Type" "AWS::EC2::EIP"
            "DependsOn" {"Ref" "foo"}
            "Properties" {"InstanceId" "i-123"
                          "Domain" {"Fn::Join" ["" ["v" "p" "c"]]}}}
           (encode-resource {:parameters {:foo nil}}
                            (eip :instance-id "i-123"
                                 :domain [:fn
                                          [:join
                                           {:delimiter ""
                                            :values ["v" "p" "c"]}]])
                            :depends-on [:ref :foo])))))






