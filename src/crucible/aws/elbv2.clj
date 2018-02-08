(ns crucible.aws.elbv2
  "Resources in AWS::ElasticLoadBalancingV2::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(defn lbv2 [suffix] (str "AWS::ElasticLoadBalancingV2::" suffix))

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-loadbalancer.html

(s/def :crucible.aws.elbv2.load-balancer-attribute/key string?)
(s/def :crucible.aws.elbv2.load-balancer-attribute/value string?)
(s/def :crucible.aws.elbv2.tag/key string?)
(s/def :crucible.aws.elbv2.load-balancer-attribute/value string?)
(s/def ::name (spec-or-ref string?))
(s/def ::scheme (spec-or-ref #{"internet-facing" "internal"}))
(s/def ::type (spec-or-ref #{"application" "network"}))
(s/def ::ip-address-type (spec-or-ref #{"ipv4" "dualstack"}))
(s/def ::load-balancer-attribute (s/keys :opt [:crucible.aws.elbv2.load-balancer-attribute/key :crucible.aws.elbv2.load-balancer-attribute/value]))
(s/def ::load-balancer-attributes (s/* ::load-balancer-attribute))
(s/def ::security-group-id (spec-or-ref string?))
(s/def ::security-groups (s/* ::security-group-id))
(s/def ::subnet-id (spec-or-ref string?))
(s/def ::allocation-id (spec-or-ref string?))
(s/def ::subnet-mapping (s/keys :req [::subnet-id
                                      ::allocation-id]))
(s/def ::subnet-mappings (s/* ::subnet-mapping))
(s/def ::subnet (spec-or-ref string?))
(s/def ::subnets (s/* ::subnet))
(s/def ::tag (s/keys :req [:crucible.aws.elbv2.tag/key :crucible.aws.elbv2.tag/value]))
(s/def ::tags (s/* ::tag))
(s/def ::load-balancer-spec (s/keys :opt [::load-balancer-attributes
                                          ::name
                                          ::scheme
                                          ::security-groups
                                          ::subnet-mappings
                                          ::subnets
                                          ::tags
                                          ::type
                                          ::ip-address-type]))

(defresource load-balancer (lbv2 "LoadBalancer") ::load-balancer-spec)

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-listener.html

(s/def :crucible.aws.elbv2.listener-action/target-group-arn (spec-or-ref string?))
(s/def :crucible.aws.elbv2.listener-action/type (spec-or-ref #{"forward"}))
(s/def ::default-action (s/keys :req [:crucible.aws.elbv2.listener-action/target-group-arn :crucible.aws.elbv2.listener-action/type]))
(s/def ::default-actions (s/* ::default-action))
(s/def ::load-balancer-arn (spec-or-ref string?))
(s/def ::port (spec-or-ref integer?))
(s/def ::protocol (spec-or-ref #{"HTTPS" "HTTP" "TCP"}))
(s/def ::ssl-policy (spec-or-ref string?))
(s/def ::certificate-arn (spec-or-ref string?))
(s/def ::certificate (s/keys :opt [::certificate-arn]))
(s/def ::certificates (s/* ::certificate))
(s/def ::listener-spec (s/keys :req [::default-actions
                                     ::load-balancer-arn
                                     ::port
                                     ::protocol]
                               :opt [::ssl-policy
                                     ::certificates]))

(defresource listener (lbv2 "Listener") ::listener-spec)


;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-listenerrule.html

(s/def ::action (s/keys :req [:crucible.aws.elbv2.listener-action/target-group-arn
                              :crucible.aws.elbv2.listener-action/type]))
(s/def ::actions (s/* ::action))
(s/def :crucible.aws.elbv2.listener-rule-condition/field (spec-or-ref #{"host-header" "path-pattern"}))
(s/def :crucible.aws.elbv2.listener-rule-condition/values (spec-or-ref (s/* string?)))
(s/def ::condition (s/keys :opt [:crucible.aws.elbv2.listener-rule-condition/field
                                 :crucible.aws.elbv2.listener-rule-condition/values]))
(s/def ::conditions (s/* ::condition))
(s/def ::listener-arn (spec-or-ref string?))
(s/def ::priority #(s/int-in-range? 1 50000 %))
(s/def ::listener-rule (s/keys :req [::actions
                                     ::conditions
                                     ::listener-arn
                                     ::priority]))

(defresource listener-rule (lbv2 "ListenerRule") ::listener-rule)

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-listenercertificate.html

(s/def ::listener-certificate-spec (s/keys :req [::certificates
                                                 ::listener-arn]))

(defresource listener-certificate (lbv2 "ListenerCertificate") ::listener-certificate-spec)

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-targetgroup.html


(s/def :crucible.aws.elbv2.target-group-attribute/key (spec-or-ref string?))
(s/def :crucible.aws.elbv2.target-group-attribute/value (spec-or-ref string?))
(s/def :crucible.aws.elbv2.target-description/availability-zone (spec-or-ref string?))
(s/def :crucible.aws.elbv2.target-description/id (spec-or-ref string?))
(s/def :crucible.aws.elbv2.target-description/port (spec-or-ref integer?))
(s/def ::health-check-interval-seconds (spec-or-ref integer?))
(s/def ::health-check-path (spec-or-ref string?))
(s/def ::health-check-port (spec-or-ref string?)) ;; eg "traffic-port"
(s/def ::health-check-protocol (spec-or-ref #{"HTTPS" "HTTP" "TCP"}))
(s/def ::health-check-timeout-seconds (spec-or-ref #(s/int-in-range? 2 60 %)))
(s/def ::healthy-threshold-count (spec-or-ref #(s/int-in-range? 2 10 %)))
(s/def ::http-code (spec-or-ref integer?))
(s/def ::matcher (spec-or-ref (s/keys :opt [::http-code])))
(s/def ::name (spec-or-ref string?))
(s/def ::port (spec-or-ref #(s/int-in-range? 1 65535 %)))
(s/def ::protocol (spec-or-ref (spec-or-ref #{"HTTPS" "HTTP" "TCP"})))
(s/def ::tags (s/* :res/tag))
(s/def ::target-group-attribute (s/keys :opt [:crucible.aws.elbv2.target-group-attribute/key :crucible.aws.elbv2.target-group-attribute/value]))
(s/def ::target-group-attributes (s/* ::target-group-attribute))

(s/def ::target-description (s/keys :req [:crucible.aws.elbv2.target-description/id]
                                    :opt [:crucible.aws.elbv2.target-description/availability-zone
                                          :crucible.aws.elbv2.target-description/port]))
(s/def ::targets (s/* ::target-description))
(s/def ::target-type (spec-or-ref #{"instance" "ip"}))
(s/def ::unhealthy-threshold-count (spec-or-ref integer?))
(s/def ::vpc-id (spec-or-ref string?))

(s/def ::target-group-spec (s/keys :req [::vpc-id
                                         ::port
                                         ::protocol]
                                   :opt [::health-check-interval-seconds
                                         ::health-check-path
                                         ::health-check-port
                                         ::health-check-protocol
                                         ::health-check-timeout-seconds
                                         ::healthy-threshold-count
                                         ::matcher
                                         ::name
                                         ::tags
                                         ::target-group-attributes
                                         ::targets
                                         ::target-type
                                         ::unhealthy-threshold-count]))

(defresource target-group (lbv2 "TargetGroup") ::target-group-spec)
