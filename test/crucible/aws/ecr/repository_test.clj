(ns crucible.aws.ecr.repository-test
  (:require [crucible.aws.ecr.repository :as repository]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest repository-tests

  (testing "valid repository"
    (is
     (s/valid? ::repository/resource-spec
               {::repository/repository-name "infrastructure/deployment"}))))
