(ns crucible.aws.ecr
  "Resources in AWS::ECR::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(defn ecr [suffix] (str "AWS::ECR::" suffix))

(s/def :crucible.aws.ecr.lifecycle-policy/lifecycle-policy-text (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.lifecycle-policy/registry-id (spec-or-ref string?))

(s/def ::lifecycle-policy (s/keys :opt [:crucible.aws.ecr.lifecycle-policy/lifecycle-policy-text
                                        :crucible.aws.ecr.lifecycle-policy/registry-id]))
(s/def ::repository-name (spec-or-ref string?))
(s/def ::repository-policy-text (spec-or-ref map?))

(s/def ::repository (s/keys :opt [::lifecycle-policy
                                  ::repository-name
                                  ::repository-policy-text]))

(defresource repository (ecr "Repository") ::repository)
