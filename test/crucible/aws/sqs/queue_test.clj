(ns crucible.aws.sqs.queue-test
  (:require [crucible.aws.sqs.queue :as sut]
            [crucible.aws.sqs.redrive-policy :as redrive-policy]
            [crucible.core :refer [xref join] :as cf]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest listener-tests

  (testing "valid minimal queue"
    (is (s/valid? ::sut/resource-spec
                  {::sut/queue-name "webhooks"})))
  (testing "queue with redrive policy"
    (is (s/valid? ::sut/resource-spec
                  {::sut/queue-name "user-event"
                   ::sut/redrive-policy
                   {::redrive-policy/dead-letter-target-arn (xref :user-event-dl)
                    ::redrive-policy/max-receive-count 3}}))))
