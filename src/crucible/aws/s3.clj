(ns crucible.aws.s3
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.aws.iam :as iam]
            [clojure.spec :as s]))

(s/def ::arn string?)

(s/def ::value (spec-or-ref string?))

(s/def ::name (spec-or-ref #{"prefix" "suffix"}))

(s/def ::rule (s/keys :req [::name ::value]))

(s/def ::rules (s/coll-of ::rule :kind vector?))

(s/def ::s3-key (s/keys :req [::rules]))

(s/def ::filter (s/keys :req [::s3-key]))

(s/def ::event (spec-or-ref #{"s3:ObjectCreated:*"
                              "s3:ObjectCreated:Put"
                              "s3:ObjectCreated:Post"
                              "s3:ObjectCreated:Copy"
                              "s3:ObjectCreated:CompleteMultipartUpload"
                              "s3:ObjectRemoved:*"
                              "s3:ObjectRemoved:Delete"
                              "s3:ObjectRemoved:DeleteMarkerCreated"
                              "s3:ReducedRedundancyLostObject"}))

(s/def ::topic (spec-or-ref ::arn))

(s/def ::topic-configuration (s/keys :req [::event
                                           ::topic]
                                     :opt [::filter]))

(s/def ::topic-configurations (s/coll-of ::topic-configuration :kind vector?))

(s/def ::queue (spec-or-ref ::arn))

(s/def ::queue-configuration (s/keys :req [::event
                                           ::queue]
                                     :opt [::filter]))

(s/def ::queue-configurations (s/coll-of ::queue-configuration :kind vector?))

(s/def ::lambda-configuration (s/keys :req [::event
                                            ::function]
                                      :opt [::filter]))
(s/def ::function (spec-or-ref ::arn))

(s/def ::lambda-configurations (s/coll-of ::lambda-configuration :kind vector?))

(s/def ::notification-configuration (s/keys :opt [::lambda-configurations
                                                  ::queue-configurations
                                                  ::topic-configurations]))

(s/def ::max-age (spec-or-ref pos-int?))

(s/def ::id (spec-or-ref (s/and string?
                                #(< (count %) 256))))

(s/def ::exposed-headers (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::allowed-origins (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::allowed-headers (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::allowed-methods (s/coll-of (spec-or-ref #{"GET" "PUT" "HEAD" "POST" "DELETE"}) :kind vector))

(s/def ::cors-rule (s/keys :req [::allowed-methods
                                 ::allowed-origins]
                           :opt [::allowed-headers
                                 ::exposed-headers
                                 ::id
                                 ::max-age]))

(s/def ::cors-rules (s/coll-of ::cors-rule :kind vector?))

(s/def ::cors-configuration (s/keys :req [::cors-rules]))

(s/def ::bucket-name (spec-or-ref (s/and string?
                                         #(re-matches #"[a-z0-9-.]+" %))))

(s/def ::access-control #{"AuthenticatedRead"
                          "AwsExecRead"
                          "BucketOwnerRead"
                          "BucketOwnerFullControl"
                          "LogDeliveryWrite"
                          "Private"
                          "PublicRead"
                          "PublicReadWrite"})

(s/def ::s3-bucket (s/keys :opt [::bucket-name
                                 ::access-control
                                 ::cors-configuration
                                 ::lifecycle-configuration
                                 ::logging-configuration
                                 ::notification-configuration
                                 ::replication-configuration
                                 ::res/tags
                                 ::versioning-configuration
                                 ::website-configuration]))

(defresource bucket "AWS::S3::Bucket" ::s3-bucket)

(s/def ::bucket (spec-or-ref string?))

(s/def ::bucket-policy (s/keys :req [::bucket
                                     ::iam/policy-document]))

(defresource bucket-policy "AWS::S3::BucketPolicy" ::bucket-policy)
