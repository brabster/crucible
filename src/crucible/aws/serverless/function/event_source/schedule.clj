(ns crucible.aws.serverless.function.event-source.schedule
  (:require [crucible.resources :refer [spec-or-ref]]
            [clojure.spec.alpha :as s]))

(s/def ::schedule (spec-or-ref string?))

(s/def ::properties
  (s/keys :req [::schedule]))
