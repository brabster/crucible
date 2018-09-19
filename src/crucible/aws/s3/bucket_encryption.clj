(ns crucible.aws.s3.bucket-encryption
  "Amazon S3 BucketEncrtyption, a property of AWS::S3::Bucket"
  (:require [clojure.spec.alpha :as s]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(defn kms-master-key-allowed?
  [opts]
  (if (contains? opts ::kms-master-key-id)
    (= "aws:kms" (::sse-algorithm opts))
    true))

(s/def ::server-side-encryption-configuration (spec-or-ref (s/coll-of ::server-side-encryption-rule :kind vector?) ))
(s/def ::server-side-encryption-rule (s/keys :opt [::server-side-encryption-by-default]))
(s/def ::server-side-encryption-by-default (s/and
                                            (s/keys :req [::sse-algorithm] :opt [::kms-master-key-id])
                                            kms-master-key-allowed?))
(s/def ::sse-algorithm #{"AES256" "aws:kms"})
(s/def ::kms-master-key-id (spec-or-ref string?))

(defmethod ->key :sse-algorithm [_] "SSEAlgorithm")
(defmethod ->key :kms-master-key-id [_] "KMSMasterKeyID")

(s/def ::resource-property-spec (s/keys :req [::server-side-encryption-configuration]))
