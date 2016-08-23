(ns crucible.aws.cloudwatch
  "Resources in AWS::CloudWatch::*"
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(defmethod ->key ::ok-actions [_] "OKActions")

(s/def ::actions-enabled (spec-or-ref string? #_boolean?))

(s/def ::alarm-description (spec-or-ref string?))

(s/def ::alarm-name (spec-or-ref string?))

(s/def ::comparison-operator (spec-or-ref #{"GreaterThanOrEqualToThreshold"
                                            "GreaterThanThreshold"
                                            "LessThanThreshold"
                                            "LessThanOrEqualToThreshold"}))

(s/def ::value (spec-or-ref string?))

(s/def ::name (spec-or-ref string?))

(s/def ::dimension (s/keys :req [::name ::value]))

(s/def ::dimensions (s/coll-of ::dimension :kind vector?))

(s/def ::evaluation-periods (spec-or-ref string? #_pos-int?))

(s/def ::metric-name (spec-or-ref string?))

(s/def ::namespace (spec-or-ref string?))

(s/def ::ok-actions (s/coll-of (spec-or-ref string?) :kind vector?))
(s/def ::insufficient-data-actions ::ok-actions)
(s/def ::alarm-actions (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::period (spec-or-ref string? #_(s/and pos-int?
                                              #(= 0 (mod % 60)))))

(s/def ::statistic (spec-or-ref #{"SampleCount"
                                  "Average"
                                  "Sum"
                                  "Minimum"
                                  "Maximum"}))

(s/def ::threshold (spec-or-ref string? #_number?))

(s/def ::unit (spec-or-ref #{"Seconds"
                             "Microseconds"
                             "Milliseconds"
                             "Bytes" "Kilobytes"
                             "Megabytes"
                             "Gigabytes"
                             "Terabytes"
                             "Bits"
                             "Kilobits"
                             "Megabits"
                             "Gigabits"
                             "Terabits"
                             "Percent"
                             "Count"
                             "Bytes/Second"
                             "Kilobytes/Second"
                             "Megabytes/Second"
                             "Gigabytes/Second"
                             "Terabytes/Second"
                             "Bits/Second"
                             "Kilobits/Second"
                             "Megabits/Second"
                             "Gigabits/Second"
                             "Terabits/Second"
                             "Count/Second"
                             "None"}))

(s/def ::alarm (s/keys :req [::comparison-operator
                             ::evaluation-periods
                             ::metric-name
                             ::namespace
                             ::period
                             ::statistic
                             ::threshold]
                       :opt [::actions-enabled
                             ::alarm-actions
                             ::alarm-description
                             ::alarm-name
                             ::dimensions
                             ::insufficient-data-actions
                             ::ok-actions
                             ::unit]))

(defresource alarm "AWS::CloudWatch::Alarm" ::alarm)
