(ns crucible.aws.ec2.route-test
  (:require [crucible.aws.ec2.route :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest route-tests

  (testing "valid gateway id" (is (s/valid? ::sut/gateway-id "sample-gateway-id")))

  (testing "nat route" (is (s/valid? ::sut/route {::sut/route-table-id "some-routetable"
                                                         ::sut/nat-gateway-id "some-gateway"
                                                         ::sut/destination-cidr-block "0.0.0.0/0"}))))
