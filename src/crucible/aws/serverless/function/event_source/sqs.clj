(ns crucible.aws.serverless.function.event-source.sqs
  (:require [crucible.aws.serverless :as sam]
            [crucible.resources :refer [spec-or-ref]]
            [clojure.spec.alpha :as s]))

(s/def ::queue ::sam/arn)

(s/def ::batch-size (spec-or-ref integer?))

(s/def ::properties
  (s/keys :req [::queue]
          :opt [::batch-size]))
