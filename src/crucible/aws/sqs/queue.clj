(ns crucible.aws.sqs.queue
  "AWS::SQS::Queue"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.aws.sqs.redrive-policy :as redrive-policy]))

(s/def ::content-based-deduplication (spec-or-ref boolean?))
(s/def ::delay-seconds (spec-or-ref integer?))
(s/def ::fifo-queue (spec-or-ref boolean?))
(s/def ::kms-master-key-id (spec-or-ref string?))
(s/def ::maximum-message-size (spec-or-ref integer?))
(s/def ::message-retention-period (spec-or-ref integer?))
(s/def ::queue-name (spec-or-ref string?))
(s/def ::receive-message-wait-time-seconds (spec-or-ref integer?))
(s/def ::redrive-policy (spec-or-ref ::redrive-policy/resource-property-spec))
(s/def ::visibility-timeout (spec-or-ref integer?))

(s/def ::resource-spec (s/keys :opt [::content-based-deduplication
                                     ::delay-seconds
                                     ::fifo-queue
                                     ::kms-master-key-id
                                     ::kms-data-key-reuse-period-sec
                                     ::maximum-message-size
                                     ::message-retention-period
                                     ::queue-name
                                     ::receive-message-wait-time-seconds
                                     ::redrive-policy
                                     ::visibility-timeout]))
