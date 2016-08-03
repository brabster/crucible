(ns crucible.aws.lambda
  (:require [crucible.resources :refer [resource-factory]]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(defmethod ->key ::s3-bucket [_] "S3Bucket")
(defmethod ->key ::s3-key [_] "S3Key")
(defmethod ->key ::s3-object-version [_] "S3ObjectVersion")

(s/def ::subnet-ids ::v/value)

(s/def ::security-group-ids ::v/value)

(s/def ::vpc-config (s/keys :req [::security-group-ids]
                            :opt [::subnet-ids]))

(s/def ::timeout (s/or :compiled (s/and
                                  pos-int?
                                  #(<= % (* 5 60)))
                       :runtime ::v/value))

(s/def ::runtime #{"nodejs" "nodejs4.3" "java8" "python2.7"})

(s/def ::role ::v/value)

(s/def ::memory-size (s/or :compiled (s/and
                                      #(-> % (mod 64) (= 0))
                                      #(<= 128 % 1536))
                           :runtime ::v/value))

(s/def ::handler ::v/value)

(s/def ::function-name ::v/value)

(s/def ::description ::v/value)

(s/def ::s3-bucket ::v/value)
(s/def ::s3-key ::v/value)
(s/def ::s3-object-version ::v/value)
(s/def ::zip-file ::v/value)

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

(s/def ::batch-size (s/or :compiled (s/and pos-int?
                                           #(< % 10000))
                          :runtime ::v/value))

(s/def ::enabled (s/or :compiled boolean?
                       :runtime ::v/value))

(s/def ::event-source-arn (s/or :compiled string?
                                :runtime ::v/value))

(s/def ::starting-postition (s/or :compiled #{"TRIM_HORIZON" "LATEST"}
                                  :runtime ::v/value))

(s/def ::event-source-mapping (s/keys :req [::event-source-arn
                                            ::function-name
                                            ::starting-position]
                                      :opt [::batch-size
                                            ::enabled]))

(def event-source-mapping (resource-factory "AWS::Lambda::EventSourceMapping" ::event-source-mapping))

