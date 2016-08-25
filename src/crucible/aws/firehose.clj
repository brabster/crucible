(ns crucible.aws.firehose
  "Resources in AWS::KinesisFirehose::DeliveryStream"
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [clojure.spec :as s]
            [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :size-in-mbs [_]
  "SizeInMBs")

(defmethod ->key :kms-encryption-config [_]
  "KMSEncryptionConfig")

(defmethod ->key :aws-kms-key-arn [_]
  "AWSKMSKeyARN")

(defmethod ->key :bucket-arn [_]
  "BucketARN")

(s/def ::arn (spec-or-ref string?))

(s/def ::delivery-stream-name (spec-or-ref string?))

(s/def ::bucket-arn ::arn)

(s/def ::interval-in-seconds (spec-or-ref pos-int?))

(s/def ::size-in-mbs (spec-or-ref pos-int?))

(s/def ::buffering-hints (s/keys :req [::interval-in-seconds
                                       ::size-in-mbs]))

(s/def ::compression-format (spec-or-ref #{"UNCOMPRESSED" "GZIP" "ZIP" "Snappy"}))

(s/def ::prefix (spec-or-ref string?))

(s/def ::role-arn ::arn)

(s/def ::enabled (spec-or-ref boolean?))

(s/def ::log-group-name (spec-or-ref string?))

(s/def ::log-stream-name (spec-or-ref string?))

(s/def ::cloud-watch-logging-options (s/keys :opt [::enabled
                                                   ::log-group-name
                                                   ::log-stream-name]))

(s/def ::aws-kms-key-arn ::arn)

(s/def ::kms-encryption-config  (s/keys :req [::aws-kms-key-arn]))

(s/def ::encryption-configuration (s/keys :opt [::kms-encryption-config
                                                ::no-encryption-config]))

(s/def ::s3-destination-configuration (s/keys :req [::bucket-arn
                                                   ::buffering-hints
                                                   ::compression-format
                                                   ::prefix
                                                   ::role-arn]
                                       :opt [::cloud-watch-logging-options
                                             ::encryption-configuration]))

(s/def ::firehose (s/keys :req [::delivery-stream-name
                                ::s3-destination-configuration]))

(defresource firehose "AWS::KinesisFirehose::DeliveryStream" ::firehose)