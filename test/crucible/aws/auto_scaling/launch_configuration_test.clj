(ns crucible.aws.auto-scaling.launch-configuration-test
  (:require [crucible.aws.auto-scaling.launch-configuration :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest launch-configuration-tests
  (testing "valid launch configuration"
    (is (s/valid? ::sut/launch-configuration
                  {::sut/min-size "2"
                   ::sut/max-size "2"})))
  (testing "invalid launch configuration"
    (is (not (s/valid? ::sut/launch-configuration
                       {})))))
