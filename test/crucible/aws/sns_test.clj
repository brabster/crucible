(ns crucible.aws.sns-test
  (:require [crucible.aws.sns :as sns]
            [crucible.assertion :refer [resource=]]
            [clojure.test :refer :all]))

(deftest sns-topic-test
  (testing "encode"
    (is (resource= {"Type" "AWS::SNS::Topic",
                    "Properties"
                    {"TopicName" "SampleTopic"}}
                   (sns/topic {::sns/topic-name "SampleTopic"})))))
