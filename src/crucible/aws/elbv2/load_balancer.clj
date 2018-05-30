(ns crucible.aws.elbv2.load-balancer
  "AWS::ElasticLoadBalancingV2::LoadBalancer"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.aws.elbv2 :as elbv2]))

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-loadbalancer.html

(s/def ::key string?)
(s/def ::value string?)
(s/def ::name (spec-or-ref string?))
(s/def ::scheme (spec-or-ref #{"internet-facing" "internal"}))
(s/def ::type (spec-or-ref #{"application" "network"}))
(s/def ::ip-address-type (spec-or-ref #{"ipv4" "dualstack"}))
(s/def ::load-balancer-attribute (s/keys :opt [::key ::value]))
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
(s/def ::load-balancer-spec (s/keys :opt [::load-balancer-attributes
                                          ::name
                                          ::scheme
                                          ::security-groups
                                          ::subnet-mappings
                                          ::subnets
                                          ::res/tags
                                          ::type
                                          ::ip-address-type]))

(defresource load-balancer (elbv2/prefix "LoadBalancer") ::load-balancer-spec)
