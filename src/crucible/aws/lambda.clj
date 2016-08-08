(ns crucible.aws.lambda
  (:require [crucible.resources :refer [resource-factory]]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(defmethod ->key ::s3-bucket [_] "S3Bucket")
(defmethod ->key ::s3-key [_] "S3Key")
(defmethod ->key ::s3-object-version [_] "S3ObjectVersion")

(s/def ::subnet-id (v/spec-or-ref string?))

(s/def ::security-group-ids (v/spec-or-ref string?))

(s/def ::vpc-config (s/keys :req [::security-group-ids]
                            :opt [::subnet-ids]))

(s/def ::timeout (v/spec-or-ref (s/and pos-int? #(<= % (* 5 60)))))

(s/def ::runtime #{"nodejs" "nodejs4.3" "java8" "python2.7"})

(s/def ::role (v/spec-or-ref string?))

(s/def ::memory-size (v/spec-or-ref (s/and #(-> % (mod 64) (= 0)) #(<= 128 % 1536))))

(s/def ::handler (v/spec-or-ref string?))

(s/def ::function-name (v/spec-or-ref string?))

(s/def ::description (v/spec-or-ref string?))

(s/def ::s3-bucket (v/spec-or-ref string?))
(s/def ::s3-key (v/spec-or-ref string?))
(s/def ::s3-object-version (v/spec-or-ref string?))
(s/def ::zip-file (v/spec-or-ref string?))

(s/def ::code (s/keys ::opt [::s3-bucket
                             ::s3-key
                             ::s3-object-version
                             ::zip-file]))

(s/def ::function (s/keys :req [::code
                                ::handler
                                ::role
                                ::runtime]
                          :opt [::function-name
                                ::description
                                ::memory-size
                                ::runtime
                                ::vpc-config]))

(def function (resource-factory "AWS::Lambda::Function" ::function))

(s/def ::batch-size (v/spec-or-ref (s/and pos-int? #(< % 10000))))

(s/def ::enabled (v/spec-or-ref boolean?))

(s/def ::event-source-arn (v/spec-or-ref string?))

(s/def ::starting-postition (v/spec-or-ref #{"TRIM_HORIZON" "LATEST"}))

(s/def ::event-source-mapping (s/keys :req [::event-source-arn
                                            ::function-name
                                            ::starting-position]
                                      :opt [::batch-size
                                            ::enabled]))

(def event-source-mapping (resource-factory "AWS::Lambda::EventSourceMapping" ::event-source-mapping))

