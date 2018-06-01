(ns crucible.aws.ecr.repository
  "AWS::ECR::Repository"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref] :as res]))

(s/def ::lifecycle-policy-text (spec-or-ref string?))
(s/def ::registry-id (spec-or-ref string?))

(s/def ::lifecycle-policy (s/keys :opt [::lifecycle-policy-text
                                        ::registry-id]))
(s/def ::repository-name (spec-or-ref string?))
(s/def ::repository-policy-text (spec-or-ref map?))

(s/def ::resource-spec (s/keys :opt [::lifecycle-policy
                                     ::repository-name
                                     ::repository-policy-text]))
