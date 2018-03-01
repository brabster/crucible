(ns crucible.aws.ecs-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.ecs :as ecs]
             [crucible.resources :as res]
             [clojure.spec.alpha :as s]
             [crucible.core :refer [template encode parameter xref]]))

(deftest ecs-cluster-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ecs/cluster {::ecs/cluster-name "cluster-one"}))))))
