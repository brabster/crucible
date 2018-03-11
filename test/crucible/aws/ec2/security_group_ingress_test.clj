(ns crucible.aws.ec2.security-group-ingress-test
  (:require [crucible.aws.ec2.security-group-ingress :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest security-group-ingress-tests

  (testing "valid security group ingress rule"
    (is (s/valid? ::sut/security-group-ingress {::sut/ip-protocol "tcp"
                                                ::sut/from-port 4334
                                                ::sut/to-port 4336
                                                ::sut/source-security-group-id "some-security-group-id"
                                                ::sut/group-id "some-group-id"}))))
