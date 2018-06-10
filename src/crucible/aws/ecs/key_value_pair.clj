(ns crucible.aws.ecs.key-value-pair
  "AWS::ECS::TaskDefinition > ContainerDefinition > KeyValuePair"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::name (spec-or-ref string?))
(s/def ::value (spec-or-ref string?))

(s/def ::entity-spec (s/keys :req [::name
                                   ::value]))
