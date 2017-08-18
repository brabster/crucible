(ns crucible.aws.events
  "Resources in AWS::Events::*"
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec.alpha :as s]))

(s/def ::id (spec-or-ref #(re-matches #"[\.\-_A-Za-z0-9]+" %)))

(s/def ::arn (spec-or-ref string?))

(s/def ::target (s/keys :req [::arn ::id]))

(s/def ::targets (s/coll-of ::target :kind vector?))

(s/def ::state (spec-or-ref #{"ENABLED" "DISABLED"}))

(s/def ::schedule-expression (spec-or-ref string?))

(s/def ::role-arn (spec-or-ref string?))

(s/def ::event-pattern (spec-or-ref any?))

(s/def ::name (spec-or-ref string?))

(s/def ::description (spec-or-ref string?))

(s/def ::rule (s/keys :opt [::description
                            ::event-pattern
                            ::name
                            ::role-arn
                            ::schedule-expression
                            ::state
                            ::targets]))

(defresource rule "AWS::Events::Rule" ::rule)
