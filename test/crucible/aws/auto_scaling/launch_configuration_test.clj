(ns crucible.aws.auto-scaling.launch-configuration-test
  (:require [crucible.aws.auto-scaling.launch-configuration :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest launch-configuration-tests
  (testing "valid launch configuration"
    (is (s/valid? ::sut/resource-spec
                  {::sut/key-name "secret"
                   ::sut/image-id "ami-something"
                   ::sut/instance-type (xref :instance-type)})))
  (testing "invalid launch configuration"
    (is (not (s/valid? ::sut/resource-spec
                       {})))))
