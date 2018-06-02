(ns crucible.aws.route53.record-set-test
  (:require [crucible.aws.route53.record-set :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest route-tests

  (testing "valid record set"
    (is (s/valid? ::sut/resource-spec
                  {::sut/name "www.crucible.io"
                   ::sut/type "A"
                   ::sut/hosted-zone-name "atlascrm.io."
                   ::sut/alias-target {::sut/hosted-zone-id (xref :load-balancer :canonical-hosted-zone-name-i-d)
                                       ::sut/evaluate-target-health "false"
                                       ::sut/dns-name (xref :load-balancer :canonical-hosted-zone-name)}}))))
