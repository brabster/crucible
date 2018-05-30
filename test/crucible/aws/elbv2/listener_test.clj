(ns crucible.aws.elbv2.listener-test
  (:require [crucible.aws.elbv2.listener :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest listener-tests

  (testing "valid listener"
    (is (s/valid? ::sut/listener-spec
                  {::sut/default-actions [{::sut/target-group-arn (xref :target-group)
                                           ::sut/type "forward"}]
                   ::sut/load-balancer-arn (xref :load-balancer)
                   ::sut/port 443
                   ::sut/protocol "HTTPS"
                   ::sut/certificates [{::sut/certificate-arn
                                        (cf/find-in-map :environments cf/stack-name "CertificateArn")}]}))))
