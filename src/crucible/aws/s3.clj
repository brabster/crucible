(ns crucible.aws.s3
  (:require [crucible.resources :as r]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(defmethod ->key ::s3-key [_] "S3Key")

(s/def ::arn string?)

(s/def ::value (v/spec-or-ref string?))

(s/def ::name (v/spec-or-ref #{"prefix" "suffix"}))

(s/def ::rule (s/keys :req [::name ::value]))

(s/def ::rules (s/coll-of ::rule :kind vector?))

(s/def ::s3-key (s/keys :req [::rules]))

(s/def ::filter (s/keys :req [::s3-key]))

(s/def ::event (v/spec-or-ref #{"s3:ObjectCreated:*"
                                "s3:ObjectCreated:Put"
                                "s3:ObjectCreated:Post"
                                "s3:ObjectCreated:Copy"
                                "s3:ObjectCreated:CompleteMultipartUpload"
                                "s3:ObjectRemoved:*"
                                "s3:ObjectRemoved:Delete"
                                "s3:ObjectRemoved:DeleteMarkerCreated"
                                "s3:ReducedRedundancyLostObject"}))

(s/def ::topic (v/spec-or-ref ::arn))

(s/def ::topic-configuration (s/keys :req [::event
                                           ::topic]
                                     :opt [::filter]))

(s/def ::topic-configurations (s/coll-of ::topic-configuration :kind vector?))

(s/def ::queue (v/spec-or-ref ::arn))

(s/def ::queue-configuration (s/keys :req [::event
                                           ::queue]
                                     :opt [::filter]))

(s/def ::queue-configurations (s/coll-of ::queue-configuration :kind vector?))

(s/def ::lambda-configuration (s/keys :req [::event
                                            ::function]
                                      :opt [::filter]))
(s/def ::function (v/spec-or-ref ::arn))

(s/def ::lambda-configurations (s/coll-of ::lambda-configuration :kind vector?))

(s/def ::notification-configuration (s/keys :opt [::lambda-configurations
                                                  ::queue-configurations
                                                  ::topic-configurations]))

(s/def ::max-age (v/spec-or-ref pos-int?))

(s/def ::id (v/spec-or-ref (s/and string?
                                  #(< (count %) 256))))

(s/def ::exposed-headers (s/coll-of (v/spec-or-ref string?) :kind vector?))

(s/def ::allowed-origins (s/coll-of (v/spec-or-ref string?) :kind vector?))

(s/def ::allowed-headers (s/coll-of (v/spec-or-ref string?) :kind vector?))

(s/def ::allowed-methods (s/coll-of (v/spec-or-ref #{"GET" "PUT" "HEAD" "POST" "DELETE"}) :kind vector))

(s/def ::cors-rule (s/keys :req [::allowed-methods
                                 ::allowed-origins]
                           :opt [::allowed-headers
                                 ::exposed-headers
                                 ::id
                                 ::max-age]))

(s/def ::cors-rules (s/coll-of ::cors-rule :kind vector?))

(s/def ::cors-configuration (s/keys :req [::cors-rules]))

(s/def ::bucket-name (v/spec-or-ref (s/and string?
                                           #(re-matches #"[a-z0-9-.]+" %))))

(s/def ::access-control #{"AuthenticatedRead"
                          "AwsExecRead"
                          "BucketOwnerRead"
                          "BucketOwnerFullControl"
                          "LogDeliveryWrite"
                          "Private"
                          "PublicRead"
                          "PublicReadWrite"})

(s/def ::bucket (s/keys :opt [::bucket-name
                              ::access-control
                              ::cors-configuration
                              ::lifecycle-configuration
                              ::logging-configuration
                              ::notification-configuration
                              ::replication-configuration
                              ::r/tags
                              ::versioning-configuration
                              ::website-configuration]))

(def bucket (r/resource-factory "AWS::S3::Bucket" ::bucket))
