(ns crucible.aws.auto-scaling.auto-scaling-group-test
  (:require [crucible.aws.auto-scaling.auto-scaling-group :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest auto-scaling-group-tests
  (testing "valid auto scaling group"
    (is (s/valid? ::sut/resource-spec
                  {::sut/min-size "2"
                   ::sut/max-size "2"})))
  (testing "invalid auto scaling group"
    (is (not (s/valid? ::sut/resource-spec
                       {})))))
