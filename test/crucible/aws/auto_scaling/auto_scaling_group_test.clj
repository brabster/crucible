(ns crucible.aws.auto-scaling.auto-scaling-group-test
  (:require [crucible.aws.auto-scaling.auto-scaling-group :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest auto-scaling-group-tests
  (testing "valid auto scaling group"
    (is (s/valid? ::sut/resource-spec
                  {::sut/min-size "2"
                   ::sut/max-size "2"
                   ::sut/availability-zones ["us-east-1"]
                   ::sut/tags [{::sut/key "Name"
                                ::sut/value "Testing"
                                ::sut/propagate-at-launch true
                                ::sut/resource-type "auto-scaling-group"
                                ::sut/resource-id "my-asg"}]})))
  (testing "invalid auto scaling group"
    (is (not (s/valid? ::sut/resource-spec
                       {})))))
