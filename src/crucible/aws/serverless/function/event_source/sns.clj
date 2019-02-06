(ns crucible.aws.serverless.function.event-source.sns
  (:require [crucible.aws.serverless :as sam]
            [crucible.resources :refer [spec-or-ref]]
            [clojure.spec.alpha :as s]))

(s/def ::topic ::sam/arn)

(s/def ::properties
  (s/keys :req [::topic]))
