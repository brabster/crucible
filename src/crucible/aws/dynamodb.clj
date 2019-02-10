(ns crucible.aws.dynamodb
  "Resources in AWS::DynamoDB::*"
  (:require [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource]]
            [clojure.spec.alpha :as s]))

(s/def ::table-name (spec-or-ref string?))

(s/def ::attribute-name (spec-or-ref string?))

(s/def ::attribute-type (spec-or-ref #{"S" "N" "B"}))

(s/def ::attribute-definition (s/keys :req [::attribute-name
                                            ::attribute-type]))

(s/def ::attribute-definitions (s/* ::attribute-definition))

(s/def ::index-name (spec-or-ref string?))

(s/def ::key-type (spec-or-ref #{"HASH" "RANGE"}))

(s/def ::hash-key (s/keys :req [::attribute-name
                                ::key-type]))

(s/def ::range-key (s/keys :req [::attribute-name
                                 ::key-type]))

(s/def ::key-schema (s/cat :hash ::hash-key
                           :range (s/? ::range-key)))

(s/def ::non-key-attributes (s/* (spec-or-ref string?)))

(s/def ::projection-type (spec-or-ref #{"KEYS_ONLY" "INCLUDE" "ALL"}))

(s/def ::projection (s/keys :opt [::non-key-attributes
                                  ::projection-type]))

(s/def ::capacity-units (spec-or-ref (s/or :string string?
                                           :integer int?)))

(s/def ::read-capacity-units ::capacity-units)

(s/def ::write-capacity-units ::capacity-units)

(s/def ::provisioned-throughput (s/keys :req [::read-capacity-units
                                              ::write-capacity-units]))

(s/def ::global-secondary-index (s/keys :req [::index-name
                                              ::key-schema
                                              ::projection
                                              ::provisioned-throughput]))

(s/def ::global-secondary-indexes (s/* ::global-secondary-index))

(s/def ::local-secondary-index (s/keys :req [::index-name
                                             ::key-schema
                                             ::projection]))

(s/def ::local-secondary-indexes (s/* ::local-secondary-index))

(s/def ::stream-view-type (spec-or-ref #{"KEYS_ONLY"
                                         "NEW_IMAGE"
                                         "OLD_IMAGE"
                                         "NEW_AND_OLD_IMAGES"}))

(s/def ::stream-specification (s/keys :req [::stream-view-type]))

(s/def ::sse-enabled (spec-or-ref boolean?))

(defmethod ->key ::sse-enabled [_] "SSEEnabled")

(s/def ::sse-specification
  (s/keys :req [::sse-enabled]))

(defmethod ->key ::sse-specification [_] "SSESpecification")

(s/def ::table (s/keys :req [::attribute-definitions
                             ::key-schema
                             ::provisioned-throughput]
                       :opt [::table-name
                             ::stream-specification
                             ::global-secondary-indexes
                             ::local-secondary-indexes
                             ::sse-specification]))

(defresource table "AWS::DynamoDB::Table" ::table)
