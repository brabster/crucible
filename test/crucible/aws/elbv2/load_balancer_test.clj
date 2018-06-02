(ns crucible.aws.elbv2.load-balancer-test
  (:require [crucible.aws.elbv2.load-balancer :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest load-balancer-tests

  (testing "valid load balancer"
    (is (s/valid? ::sut/resource-spec
                  {::sut/name (join "-" [cf/stack-name "crucible"])
                   ::sut/subnets [(xref :public1) (xref :public2)]
                   ::sut/security-groups [(xref :sg-public)]}))))
