(ns crucible.aws.sqs.redrive-policy
  "Amazon SQS RedrivePolicy, a property of AWS::SQS::Queue"
  (:require [clojure.spec.alpha :as s]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(s/def ::dead-letter-target-arn (spec-or-ref string?))
(s/def ::max-receive-count (spec-or-ref integer?))

(defmethod ->key :max-receive-count [_] "maxReceiveCount")
(defmethod ->key :dead-letter-target-arn [_] "deadLetterTargetArn")

(s/def ::resource-property-spec (s/keys :req [::dead-letter-target-arn
                                              ::max-receive-count]))
