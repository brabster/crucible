(ns crucible.aws.elbv2.listener
  "AWS::ElasticLoadBalancingV2::Listener"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.elbv2 :as elbv2]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-listener.html

(s/def ::target-group-arn (spec-or-ref string?))
(s/def ::type (spec-or-ref #{"forward"}))
(s/def ::default-action (s/keys :req [::target-group-arn ::type]))
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

(defresource listener (elbv2/prefix "Listener") ::listener-spec)
