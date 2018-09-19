(ns crucible.aws.kms.key-test
  (:require [crucible.aws.kms.key :as sut]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest key-tests
  (testing "valid key"
    (is (s/valid? ::sut/resource-spec
                  {::sut/description (join "-" [cf/stack-name "master-key"])
                   ::sut/key-policy {:version "2012-10-17"}}))))
