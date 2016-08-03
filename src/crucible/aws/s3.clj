(ns crucible.aws.s3
  (:require [crucible.resources :as r]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(s/def ::bucket-name (v/spec-or-ref (s/and string?
                                           #(re-matches #"[a-z0-9-.]+" %))))

(s/def ::access-control #{"AuthenticatedRead" "AwsExecRead" "BucketOwnerRead" "BucketOwnerFullControl" "LogDeliveryWrite" "Private" "PublicRead" "PublicReadWrite"})

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
