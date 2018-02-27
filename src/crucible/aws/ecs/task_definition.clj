(ns crucible.aws.ecs.task-definition
  "Resources in AWS::ECS::TaskDefinition"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.ecs.container-definition :as cd]))

(s/def ::container-definitions (s/coll-of ::cd/container-definition :kind vector?))

(s/def ::cpu (spec-or-ref string?))

(s/def ::execution-role-arn (spec-or-ref string?))

(s/def ::family (spec-or-ref string?))

(s/def ::memory (spec-or-ref string?))

(s/def ::network-mode (spec-or-ref string?))

(s/def ::requires-compatibilities (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::task-role-arn (spec-or-ref string?))

(s/def ::name (spec-or-ref string?))
(s/def ::host (spec-or-ref string?))

(s/def ::task-definition-volume (s/keys :req [::name]
                                        :opt [::host]))

(s/def ::volumes (s/coll-of ::task-definition-volume :kind vector?))

(s/def ::task-definition (s/keys :req [::container-definitions]
                                 :opt [::cpu
                                       ::execution-role-arn
                                       ::family
                                       ::memory
                                       ::network-mode
                                       ::placement-constraints
                                       ::requires-compatibilities
                                       ::task-role-arn
                                       ::volumes]))