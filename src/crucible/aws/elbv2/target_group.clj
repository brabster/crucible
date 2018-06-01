(ns crucible.aws.elbv2.target-group
  "AWS::ElasticLoadBalancingV2::TargetGroup"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

;; http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-elasticloadbalancingv2-targetgroup.html


(s/def ::key (spec-or-ref string?))
(s/def ::value (spec-or-ref string?))
(s/def ::availability-zone (spec-or-ref string?))
(s/def ::id (spec-or-ref string?))
(s/def ::port (spec-or-ref integer?))
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
(s/def ::target-group-attribute (s/keys :opt [::key ::value]))
(s/def ::target-group-attributes (s/* ::target-group-attribute))

(s/def ::target-description (s/keys :req [::id]
                                    :opt [::availability-zone
                                          ::port]))
(s/def ::targets (s/* ::target-description))
(s/def ::target-type (spec-or-ref #{"instance" "ip"}))
(s/def ::unhealthy-threshold-count (spec-or-ref integer?))
(s/def ::vpc-id (spec-or-ref string?))

(s/def ::resource-spec (s/keys :req [::vpc-id
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
                                         ::res/tags
                                         ::target-group-attributes
                                         ::targets
                                         ::target-type
                                         ::unhealthy-threshold-count]))
