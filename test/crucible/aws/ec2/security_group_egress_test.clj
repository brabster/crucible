(ns crucible.aws.ec2.security-group-egress-test
  (:require [crucible.aws.ec2.security-group-egress :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest security-group-egress-tests

  (testing "valid security group egress rule"
    (is (s/valid? ::sut/security-group-egress {::sut/ip-protocol "tcp"
                                               ::sut/from-port 3000
                                               ::sut/to-port 3000
                                               ::sut/group-id "some-group-id"
                                               ::sut/destination-security-group-id "some-security-group-id"}))))
