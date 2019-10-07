(ns crucible.aws.cloudwatch.anomaly-detector
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.cloudwatch :as cloudwatch]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [defresource spec-or-ref]]))

(s/def ::value (spec-or-ref string?))

(s/def ::name (spec-or-ref string?))

(s/def ::dimension (s/keys :req [::name ::value]))

(s/def ::dimensions (s/coll-of ::dimension :kind vector?))

(s/def ::stat ::cloudwatch/statistic)

(def date-time-regex
  #"(19|20)[0-9][0-9]-(0[0-9]|1[0-2])-(0[1-9]|([12][0-9]|3[01]))T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]")

(s/def ::date-time (spec-or-ref (s/and string? #(re-matches date-time-regex %))))

(s/def ::end-time ::date-time)

(s/def ::start-time ::date-time)

(s/def ::range (s/keys :req [::start-time ::end-time]))

(s/def ::ranges (s/coll-of ::range))

(s/def ::namespace (spec-or-ref string?))

(s/def ::metric-name (spec-or-ref string?))

(s/def ::excluded-time-ranges (spec-or-ref ::ranges))

(s/def ::metric-time-zone (spec-or-ref string?))

(s/def ::configuration (s/keys :opt [::excluded-time-ranges ::metric-time-zone]))

(s/def ::anomaly-detector (s/keys :req [::metric-name
                                        ::namespace
                                        ::stat]))

(defresource anomaly-detector "AWS::CloudWatch::AnomalyDetector" ::anomaly-detector)
