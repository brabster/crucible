(ns crucible.aws.events-test
  (:require [crucible.aws.events :as events]
            [crucible.assertion :refer [resource=]]
            [crucible.core :refer [parameter xref]]
            [cheshire.core :as json]
            [clojure.test :refer :all :as t]
            [clojure.java.io :as io]))

(deftest minimal-events-test
  (testing "encode"
    (is (=
         (json/decode (slurp (io/resource "aws/events/simple-event.json")))
         (crucible.encoding/rewrite-element-data
          (events/rule {::events/description "EventRule"
                        ::events/event-pattern {"detail-type"
                                                [ "AWS API Call via CloudTrail" ]
                                                "detail" {"userIdentity" {"type" ["Root"]}}}
                        ::events/state "ENABLED"
                        ::events/targets [{::events/arn (xref :my-sns-topic)
                                           ::events/id "OpsTopic"}]}))))))

(deftest target-id-regex-test
  (testing "target ID regex is required"
    (is (thrown? Exception
                 (events/rule {::events/targets [{::events/arn (xref :my-sns-topic)
                                                  ::events/id "foo bar"}]}))
        "Invalid target ID should throw exception")))
