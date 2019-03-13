(ns crucible.aws.neptune
  "Resources in AWS::Neptune::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource]]))

;;; property specs shared among AWS::Neptune::* resources

(def db-cluster-identifier-regex #"^[a-zA-Z][a-zA-Z0-9_\-]*[a-zA-Z0-9_]")
(s/def ::db-cluster-identifier
  (spec-or-ref (s/and string? #(re-matches db-cluster-identifier-regex %))))
(defmethod ->key :db-cluster-identifier [_] "DBClusterIdentifier")

(s/def ::db-subnet-group-name (spec-or-ref string?))
(defmethod ->key :db-subnet-group-name [_] "DBSubnetGroupName")

(s/def ::description (spec-or-ref string?))

(s/def ::family (spec-or-ref #{"neptune1"}))

(s/def ::name (spec-or-ref string?))

(s/def ::parameters (spec-or-ref (s/map-of string? string?)))

