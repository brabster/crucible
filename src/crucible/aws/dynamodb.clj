(ns crucible.aws.dynamodb
  (:require [crucible.resources :as r]
            [crucible.values :as v]
            [clojure.spec :as s]))

(s/def ::table-name ::v/value)

(s/def ::attribute-name string?)

(s/def ::attribute-type #{"S" "N" "B"})

(s/def ::attribute-definition (s/keys :req [::attribute-name
                                            ::attribute-type]))

(s/def ::attribute-definitions (s/* ::attribute-definition))

(s/def ::index-name string?)

(s/def ::key-type #{"HASH" "RANGE"})

(s/def ::hash-key (s/keys :req [::attribute-name
                                ::key-type]))

(s/def ::range-key (s/keys :req [::attribute-name
                                 ::key-type]))

(s/def ::key-schema (s/cat :hash ::hash-key
                           :range (s/? ::range-key)))

(s/def ::non-key-attributes (s/* string?))

(s/def ::projection-type #{"KEYS_ONLY" "INCLUDE" "ALL"})

(s/def ::projection (s/keys :opt [::non-key-attributes
                                  ::projection-type]))

(s/def ::capacity-units ::v/value)

(s/def ::read-capacity-units ::capacity-units)

(s/def ::write-capacity-units ::capacity-units)

(s/def ::provisioned-throughput (s/keys :req [::read-capacity-units
                                              ::write-capacity-units]))

(s/def ::global-secondary-index (s/keys :req [::index-name
                                              ::key-schema
                                              ::projection
                                              ::provisioned-throughput]))

(s/def ::global-secondary-index (s/* ::global-secondary-index))

(s/def ::local-secondary-index (s/keys :req [::index-name
                                             ::key-schema
                                             ::projection]))

(s/def ::local-secondary-indexes (s/* ::local-secondary-index))

(s/def ::stream-view-type #{"KEYS_ONLY"
                            "NEW_IMAGE"
                            "OLD_IMAGE"
                            "NEW_AND_OLD_IMAGES"})

(s/def ::stream-specification (s/keys :req [::stream-view-type]))

(s/def ::table (s/keys :req [::attribute-definitions
                             ::key-schema
                             ::provisioned-throughput]
                       :opt [::table-name
                             ::stream-specification
                             ::global-secondary-indexes
                             ::local-secondary-indexes]))

(def table (r/resource-factory "AWS::DynamoDB::Table" ::table))
