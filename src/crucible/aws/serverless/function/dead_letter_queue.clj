(ns crucible.aws.serverless.function.dead-letter-queue
  (:require [crucible.resources :refer [spec-or-ref]]
            [clojure.spec.alpha :as s]))

(s/def ::type #{"SNS" "SQS"})

(s/def ::target-arn (spec-or-ref string?))

(s/def ::dead-letter-queue (s/keys :req [::type
                                         ::target-arn]))
