(ns crucible.aws.elbv2
  "Resources in AWS::ElasticLoadBalancingV2::*"
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.aws.elbv2.load-balancer :as load-balancer]
            [crucible.aws.elbv2.target-group :as target-group]
            [crucible.aws.elbv2.listener :as listener]
            [crucible.aws.elbv2.listener-rule :as listener-rule]
            [crucible.aws.elbv2.listener-certificate :as listener-certificate]))

(defn prefix [suffix] (str "AWS::ElasticLoadBalancingV2::" suffix))

(defresource load-balancer (prefix "LoadBalancer") ::load-balancer/resource-spec)

(defresource target-group (prefix "TargetGroup") ::target-group/resource-spec)

(defresource listener (prefix "Listener") ::listener/resource-spec)

(defresource listener-rule (prefix "ListenerRule") ::listener-rule/resource-spec)

(defresource listener-certificate (prefix "ListenerCertificate") ::listener-certificate/resource-spec)
