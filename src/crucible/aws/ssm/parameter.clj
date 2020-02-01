(ns crucible.aws.ssm.parameter
  "AWS::SSM::Parameter"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.ssm :refer [ssm]]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(s/def ::type (spec-or-ref string?))
(s/def ::value (spec-or-ref string?))
(s/def ::allowed-pattern (spec-or-ref string?))
(s/def ::description (spec-or-ref string?))
(s/def ::name (spec-or-ref string?))
(s/def ::tier (spec-or-ref string?))

(s/def ::parameter-spec
  (s/keys :req [::type
                ::value]
          :opt [::allowed-pattern
                ::description
                ::name
                ::policies
                ::res/tags
                ::tier]))

(defresource parameter (ssm "Parameter") ::parameter-spec)
