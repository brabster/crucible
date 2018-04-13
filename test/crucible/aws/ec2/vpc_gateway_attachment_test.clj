(ns crucible.aws.ec2.vpc-gateway-attachment-test
  (:require [crucible.aws.ec2.vpc-gateway-attachment :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest vpc-gateway-attachment-tests

  (testing "valid vpc gateway attachment definition"
    (is (s/valid? ::sut/vpc-gateway-attachment {::sut/vpc-id "some-vpc-id"
                                                ::sut/internet-gateway-id "some-internet-gateway-id"}))))
