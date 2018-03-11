(ns crucible.aws.ec2.subnet-route-table-association-test
  (:require [crucible.aws.ec2.subnet-route-table-association :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest subnet-route-table-association-tests

  (testing "valid subnet route table association definition"
    (is (s/valid? ::sut/subnet-route-table-association {::sut/subnet-id "some-subnet-id"
                                                        ::sut/route-table-id "some-route-table-id"}))))
