(ns crucible.aws.events.ecs-parameters
  "AWS::ECS::Rule > EcsParameters"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.events.network-configuration :as network-configuration]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::group (spec-or-ref string?))
(s/def ::launch-type (spec-or-ref string?))
(s/def ::platform-version (spec-or-ref string?))
(s/def ::task-count (spec-or-ref integer?))
(s/def ::task-definition-arn (spec-or-ref string?))
(s/def ::network-configuration ::network-configuration/network-configuration-spec)

(s/def ::ecs-parameters-spec (s/keys :req [::task-definition-arn]
                                     :opt [::group
                                           ::launch-type
                                           ::platform-version
                                           ::task-count
                                           ::network-configuration]))
