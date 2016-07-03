(ns crucible.aws.ec2-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.ec2 :as ec2]
             [crucible.resources :as res]
             [clojure.spec :as s]))

(deftest vpc-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/vpc #::ec2{:cidr-block "1.2.3.4/24"}))))))

(deftest igw-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/internet-gateway {}))))))
