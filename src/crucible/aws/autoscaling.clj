(ns crucible.aws.autoscaling
  "Resources in AWS::AutoScaling::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(defn autoscaling [suffix] (str "AWS::AutoScaling::" suffix))

(s/def :crucible.aws.autoscaling.lifecycle-hook-specification/default-result (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.lifecycle-hook-specification/heartbeat-timeout (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.lifecycle-hook-specification/lifecycle-hook-name (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.lifecycle-hook-specification/lifecycle-transition (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.lifecycle-hook-specification/notification-metadata (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.lifecycle-hook-specification/notification-target-arn (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.lifecycle-hook-specification/role-arn (spec-or-ref string?))

(s/def :crucible.aws.autoscaling.metrics-collection/granularity (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.metrics-collection/metric (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.metrics-collection/metrics (s/* :crucible.aws.autoscaling.metrics-collection/metric))

(s/def :crucible.aws.autoscaling.notification-configuration/notification-type (spec-or-ref string?))
(s/def :crucible.aws.autoscaling.notification-configuration/notification-types (s/* :crucible.aws.autoscaling.notification-configuration/notification-type))
(s/def :crucible.aws.autoscaling.notification-configuration/topic-arn (spec-or-ref string?))

(s/def ::min-size (spec-or-ref string?))
(s/def ::max-size (spec-or-ref string?))
(s/def ::availability-zones (s/* ::availability-zone))
(s/def ::cool-down (spec-or-ref string?))
(s/def ::desired-capacity (spec-or-ref string?))
(s/def ::health-check-grace-period (spec-or-ref int?))
(s/def ::health-check-type (spec-or-ref string?))
(s/def ::instance-id (spec-or-ref string?))
(s/def ::desired-capacity (spec-or-ref string?))
(s/def ::launch-configuration-name (spec-or-ref string?))
(s/def ::lifecycle-hook-specification (s/keys :req [:crucible.aws.autoscaling.lifecycle-hook-specification/lifecycle-hook-name
                                                    :crucible.aws.autoscaling.lifecycle-hook-specification/lifecycle-transition]
                                              :opt [:crucible.aws.autoscaling.lifecycle-hook-specification/default-result
                                                    :crucible.aws.autoscaling.lifecycle-hook-specification/heartbeat-timeout
                                                    :crucible.aws.autoscaling.lifecycle-hook-specification/notification-metadata
                                                    :crucible.aws.autoscaling.lifecycle-hook-specification/notification-target-arn
                                                    :crucible.aws.autoscaling.lifecycle-hook-specification/role-arn]) )
(s/def ::lifecycle-hook-specification-list (s/* ::lifecycle-hook-specification))
(s/def ::load-balancer-name (spec-or-ref string?))
(s/def ::load-balancer-names (s/* ::load-balancer-name))
(s/def ::metric-collection (s/keys :req [:crucible.aws.autoscaling.metrics-collection/granularity]
                                   :opt [:crucible.aws.autoscaling.metrics-collection/metrics]))
(s/def ::metric-collections (s/* ::metric-collection))
(s/def ::notification-configuration (s/keys :req [:crucible.aws.autoscaling.notification-configuration/notification-types
                                                  :crucible.aws.autoscaling.notification-configuration/topic-arn]))
(s/def ::notification-configurations (s/* ::metric-collection))
(s/def ::placement-group (spec-or-ref string?))
(s/def ::target-group-arn (spec-or-ref string?))
(s/def ::target-group-arns (s/* ::target-group-arn))
(s/def ::termination-policy (spec-or-ref string?))
(s/def ::termination-policies (s/* ::termination-policy))
(s/def ::vpc-zone-id (spec-or-ref string?))
(s/def ::v-p-c-zone-identifier (s/* ::vpc-zone-id))

(s/def ::auto-scaling-group (s/keys :req [::max-size
                                          ::min-size]
                                    :opt [::availability-zones
                                          ::cool-down
                                          ::desired-capacity
                                          ::health-check-type
                                          ::health-check-grace-period
                                          ::instance-id
                                          ::desired-capacity
                                          ::launch-configuration-name
                                          ::life-cycle-hook-specification-list
                                          ::load-balancer-names
                                          ::metric-collections
                                          ::notification-configurations
                                          ::placement-group
                                          ::target-group-arns
                                          ::termination-policies
                                          ::v-p-c-zone-identifier
                                          ::res/tags]))

(defresource auto-scaling-group (autoscaling "AutoScalingGroup") ::auto-scaling-group)

(s/def ::associate-public-ip-address (spec-or-ref boolean?))
(s/def ::iam-instance-profile (spec-or-ref string?))
(s/def ::image-id (spec-or-ref string?))
(s/def ::instance-id (spec-or-ref string?))
(s/def ::instance-monitoring (spec-or-ref boolean?))
(s/def ::instance-type (spec-or-ref string?))
(s/def ::kernel-id (spec-or-ref string?))
(s/def ::key-name (spec-or-ref string?))
(s/def ::security-group (spec-or-ref string?))
(s/def ::security-groups (s/* ::security-group))
(s/def ::user-data (spec-or-ref string?))

(s/def ::launch-configuration (s/keys :req [::image-id
                                            ::instance-type]
                                      :opt [::associate-public-ip-address
                                            ::iam-instance-profile
                                            ::instance-id
                                            ::instance-monitoring
                                            ::kernel-id
                                            ::key-name
                                            ::security-groups
                                            ::user-data]))

(defresource launch-configuration (autoscaling "LaunchConfiguration") ::launch-configuration)
