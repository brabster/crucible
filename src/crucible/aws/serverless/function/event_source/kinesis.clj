(ns crucible.aws.serverless.function.event-source.kinesis
  (:require [crucible.aws.serverless :as sam]
            [crucible.resources :refer [spec-or-ref]]
            [clojure.spec.alpha :as s]))

(s/def ::stream ::sam/arn)

(s/def ::starting-position (spec-or-ref #{"TRIM_HORIZON" "LATEST"}))

(s/def ::batch-size (spec-or-ref integer?))

(s/def ::properties
  (s/keys :req [::stream
                ::starting-position]
          :opt [::batch-size]))
