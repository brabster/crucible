(ns crucible.aws.elbv2.target-group-test
  (:require [crucible.aws.elbv2.target-group :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest target-group-tests

  (testing "valid target group"
    (is (s/valid? ::sut/target-group-spec
                  {::sut/vpc-id (xref :vpc)
                   ::sut/port 80
                   ::sut/protocol "HTTP"
                   ::sut/target-type "ip"
                   ::sut/health-check-path "/ping"
                   ::sut/health-check-port "80"
                   ::sut/health-check-protocol "HTTP"
                   ::sut/target-group-attributes [{::sut/key "stickiness.enabled"
                                                   ::sut/value "true"}]}))))
