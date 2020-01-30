(ns crucible.aws.ecs.secret
  "AWS::ECS::TaskDefinition > Secret"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::name (spec-or-ref string?))
(s/def ::value-from (spec-or-ref string?))

(s/def ::secret-spec (s/keys :req [::name
                                   ::value-from]))
