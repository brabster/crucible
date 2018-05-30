(ns crucible.aws.elbv2.listener-rule
  "AWS::ElasticLoadBalancingV2::Listener"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.elbv2 :as elbv2]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-listenerrule.html

(s/def ::target-group-arn (spec-or-ref string?))
(s/def ::type (spec-or-ref #{"forward"}))
(s/def ::action (s/keys :req [::target-group-arn
                              ::type]))
(s/def ::actions (s/* ::action))
(s/def ::field (spec-or-ref #{"host-header" "path-pattern"}))
(s/def ::values (spec-or-ref (s/* string?)))
(s/def ::condition (s/keys :opt [::field
                                 ::values]))
(s/def ::conditions (s/* ::condition))
(s/def ::listener-arn (spec-or-ref string?))
(s/def ::priority #(s/int-in-range? 1 50000 %))
(s/def ::listener-rule (s/keys :req [::actions
                                     ::conditions
                                     ::listener-arn
                                     ::priority]))

(defresource listener-rule (elbv2/prefix "ListenerRule") ::listener-rule)
