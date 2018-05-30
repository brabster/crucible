(ns crucible.aws.elbv2.listener-certificate-test
  (:require [crucible.aws.elbv2.listener-certificate :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest listener-certificate-tests

  (testing "valid listener certificate"
    (is (s/valid? ::sut/listener-certificate-spec
                  {::sut/listener-arn (xref :listener-io)
                   ::sut/certificates [{::sut/certificate-arn
                                        (cf/find-in-map :environments cf/stack-name "CertificateArn")}]}))))
