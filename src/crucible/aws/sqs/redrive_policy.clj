(ns crucible.aws.sqs.redrive-policy
  "Amazon SQS RedrivePolicy, a property of AWS::SQS::Queue"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(s/def ::dead-letter-target-arn (spec-or-ref string?))
(s/def ::max-receive-count (spec-or-ref integer?))

(s/def ::resource-property-spec (s/keys :req [::dead-letter-target-arn
                                              ::max-receive-count]))
