(ns crucible.aws.ecs
  "Resources in AWS::ECS::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.ecs.task-definition :as td]
            [crucible.aws.ecs.service :as svc]))

(defn ecs [suffix] (str "AWS::ECS::" suffix))

(s/def ::cluster-name (spec-or-ref string?))

(s/def ::cluster (s/keys :req []
                     :opt [::cluster-name]))

(defresource cluster (ecs "Cluster") ::cluster)

(defresource service (ecs "Service") ::svc/service)

(defresource task-definition (ecs "TaskDefinition") ::td/task-definition)