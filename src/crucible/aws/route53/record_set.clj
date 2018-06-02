(ns crucible.aws.route53.record-set
  "AWS::Route53::RecordSet"
  (:require [clojure.spec.alpha :as s]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :as res :refer [spec-or-ref]]))

;; recordset https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-route53-recordset.html

;; alias-target
(s/def ::dns-name (spec-or-ref string?))
(s/def ::evaluate-target-health (spec-or-ref string?))

;; geo-location
(s/def ::continent-code (spec-or-ref string?))
(s/def ::country-code (spec-or-ref string?))
(s/def ::subdivision-code (spec-or-ref string?))

(s/def ::name (spec-or-ref string?))
(s/def ::alias-target (s/keys :req [::dns-name
                                    ::hosted-zone-id]
                              :opt [::evaluate-target-health]))
(s/def ::comment (spec-or-ref string?))
(s/def ::failover (spec-or-ref string?))
(s/def ::geo-location (s/keys :opt [::continent-code
                                    ::country-code
                                    ::subdivision-code]))
(s/def ::health-check-id (spec-or-ref string?))
(s/def ::hosted-zone-id (spec-or-ref string?))
(s/def ::hosted-zone-name (spec-or-ref string?))
(s/def ::region (spec-or-ref string?))
(s/def ::resource-records (spec-or-ref string?))
(s/def ::set-identifier (spec-or-ref string?))
(s/def ::ttl (spec-or-ref string?))
(s/def ::type (spec-or-ref string?))
(s/def ::weight (spec-or-ref integer?))

(s/def ::resource-spec (s/keys :req [::name]
                               :opt [::alias-target
                                     ::comment
                                     ::failover
                                     ::geo-location
                                     ::health-check-id
                                     ::hosted-zone-id
                                     ::hosted-zone-name
                                     ::region
                                     ::resource-records
                                     ::set-identifier
                                     ::ttl
                                     ::type
                                     ::weight]))

(defmethod ->key :ttl [_] "TTL")
(defmethod ->key :dns-name [_] "DNSName")
