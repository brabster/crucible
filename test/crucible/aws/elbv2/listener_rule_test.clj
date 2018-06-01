(ns crucible.aws.elbv2.listener-rule-test
  (:require [crucible.aws.elbv2.listener-rule :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest listener-rule-tests

  (testing "valid listener rule"
    (is (s/valid? ::sut/resource-spec
                  {::sut/actions [{::sut/target-group-arn (xref :target-group)
                                   ::sut/type "forward"}]
                   ::sut/conditions [{::sut/field "host-header"
                                      ::sut/values ["app.crucible.io"]}]
                   ::sut/priority 1
                   ::sut/listener-arn (xref :listener-io)}))))
