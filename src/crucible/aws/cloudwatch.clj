(ns crucible.aws.cloudwatch
  "Resources in AWS::CloudWatch::*"
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec.alpha :as s]))

(defmethod ->key :ok-actions [_] "OKActions")

(s/def ::actions-enabled (spec-or-ref boolean?))

(s/def ::actions (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::alarm-actions ::actions)

(s/def ::alarm-description (spec-or-ref string?))

(s/def ::alarm-name (spec-or-ref string?))

(s/def ::comparison-operator (spec-or-ref #{"GreaterThanOrEqualToThreshold"
                                            "GreaterThanThreshold"
                                            "LessThanThreshold"
                                            "LessThanOrEqualToThreshold"
                                            "LessThanLowerThreshold"
                                            "LessThanLowerOrGreaterThanUpperThreshold"}))

(s/def ::value (spec-or-ref string?))

(s/def ::name (spec-or-ref string?))

(s/def ::datapoints-to-alarm (spec-or-ref int?))

(s/def ::dimension (s/keys :req [::name ::value]))

(s/def ::dimensions (s/coll-of ::dimension :kind vector?))

(s/def ::evaluate-low-sample-count-percentile (spec-or-ref string?))

(s/def ::evaluation-periods (spec-or-ref pos-int?))

;; The percentile statistic for the metric. Specify a value between p0.0 and p100.
(s/def ::extended-statistic (spec-or-ref (s/and string?
                                                #(clojure.string/starts-with? % "p"))))

(s/def ::insufficient-data-actions ::actions)

(s/def ::metric-name (spec-or-ref string?))

(s/def ::expression (spec-or-ref string?))

(s/def ::id (spec-or-ref string?))

(s/def ::label (spec-or-ref string?))

(s/def ::metric (s/keys :opt [::dimensions
                              ::metric-name
                              ::namespace]))

(s/def ::metric-stat (s/keys :req [::metric
                                   ::period
                                   ::stat]
                             :opt [::unit]))

(s/def ::return-data (spec-or-ref boolean?))

(s/def ::metric-data-query (s/keys :req [::id]
                                   :opt [::expression
                                         ::label
                                         ::metric-stat
                                         ::return-data]))

(s/def ::metrics (s/coll-of ::metric-data-query))

(s/def ::namespace (spec-or-ref string?))

(s/def ::ok-actions ::actions)

;; The time over which the specified statistic is applied.
;; Specify time in seconds, in multiples of 60.
(s/def ::period (spec-or-ref (s/and pos-int?
                                    #(zero? (mod % 60)))))

(s/def ::statistic (spec-or-ref #{"SampleCount"
                                  "Average"
                                  "Sum"
                                  "Minimum"
                                  "Maximum"}))

(s/def ::threshold (spec-or-ref double?))

(s/def ::threshold-metric-id (spec-or-ref string?))

(s/def ::treat-missing-data (spec-or-ref #{"breaching"
                                           "notBreaching"
                                           "ignore"
                                           "missing"}))

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
                             ::evaluation-periods]
                       :opt [::namespace
                             ::metric-name
                             ::actions-enabled
                             ::alarm-actions
                             ::alarm-description
                             ::alarm-name
                             ::dimensions
                             ::insufficient-data-actions
                             ::ok-actions
                             ::statistic
                             ::unit
                             ::datapoints-to-alarm
                             ::evaluate-low-sample-count-percentile
                             ::metrics
                             ::period
                             ::threshold
                             ::threshold-metric-id]))

(defresource alarm "AWS::CloudWatch::Alarm" ::alarm)
