(ns crucible.aws.auto-scaling.auto-scaling-group
  "AWS::AutoScaling::AutoScalingGroup"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref] :as res]))

(s/def ::default-result (spec-or-ref string?))
(s/def ::heartbeat-timeout (spec-or-ref string?))
(s/def ::lifecycle-hook-name (spec-or-ref string?))
(s/def ::lifecycle-transition (spec-or-ref string?))
(s/def ::notification-metadata (spec-or-ref string?))
(s/def ::notification-target-arn (spec-or-ref string?))
(s/def ::role-arn (spec-or-ref string?))

(s/def ::granularity (spec-or-ref string?))
(s/def ::metric (spec-or-ref string?))
(s/def ::metrics (s/* ::metric))

(s/def ::notification-type (spec-or-ref string?))
(s/def ::notification-types (s/* ::notification-type))
(s/def ::topic-arn (spec-or-ref string?))

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
(s/def ::lifecycle-hook-specification (s/keys :req [::lifecycle-hook-name
                                                    ::lifecycle-transition]
                                              :opt [::default-result
                                                    ::heartbeat-timeout
                                                    ::notification-metadata
                                                    ::notification-target-arn
                                                    ::role-arn]) )
(s/def ::lifecycle-hook-specification-list (s/* ::lifecycle-hook-specification))
(s/def ::load-balancer-name (spec-or-ref string?))
(s/def ::load-balancer-names (s/* ::load-balancer-name))
(s/def ::metric-collection (s/keys :req [::granularity]
                                   :opt [::metrics]))
(s/def ::metric-collections (s/* ::metric-collection))
(s/def ::notification-configuration (s/keys :req [::notification-types
                                                  ::topic-arn]))
(s/def ::notification-configurations (s/* ::metric-collection))
(s/def ::placement-group (spec-or-ref string?))
(s/def ::target-group-arn (spec-or-ref string?))
(s/def ::target-group-arns (s/* ::target-group-arn))
(s/def ::termination-policy (spec-or-ref string?))
(s/def ::termination-policies (s/* ::termination-policy))
(s/def ::vpc-zone-id (spec-or-ref string?))
(s/def ::v-p-c-zone-identifier (s/* ::vpc-zone-id))

(s/def ::resource-spec (s/keys :req [::max-size
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
