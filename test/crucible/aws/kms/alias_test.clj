(ns crucible.aws.kms.alias-test
  (:require [crucible.aws.kms.alias :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest alias-tests
  (testing "valid alias"
    (is (s/valid? ::sut/resource-spec
                  {::sut/alias-name (join "-" [cf/stack-name "master-key-alias"])
                   ::sut/target-key-id (xref :env-key)}))))
