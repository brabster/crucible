(ns crucible.aws.ecs
  "Resources in AWS::ECS::*"
  (:require [crucible.resources :refer [defresource]]
            [crucible.aws.ecs.cluster :as cl]
            [crucible.aws.ecs.task-definition :as td]
            [crucible.aws.ecs.service :as svc]))

(defn ecs [suffix] (str "AWS::ECS::" suffix))

(defresource cluster (ecs "Cluster") ::cl/cluster)

(defresource service (ecs "Service") ::svc/service)

(defresource task-definition (ecs "TaskDefinition") ::td/task-definition)
