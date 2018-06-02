(ns crucible.aws.auto-scaling
  "Resources in AWS::AutoScaling::*"
  (:require [crucible.aws.auto-scaling.auto-scaling-group :as auto-scaling-group]
            [crucible.aws.auto-scaling.launch-configuration :as launch-configuration]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(defn prefix [suffix] (str "AWS::AutoScaling::" suffix))

(defresource auto-scaling-group (prefix "AutoScalingGroup") ::auto-scaling-group/resource-spec)
(defresource launch-configuration (prefix "LaunchConfiguration") ::launch-configuration/resource-spec)
