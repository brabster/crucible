(ns crucible.aws.neptune
  "Resources in AWS::Neptune::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource]]))

;;; property specs

(s/def ::allow-major-version-upgrade (spec-or-ref boolean?))

(s/def ::auto-minor-version-upgrade (spec-or-ref boolean?))

(s/def ::availability-zone (spec-or-ref string?))

(s/def ::availability-zones (spec-or-ref (s/* ::availability-zone)))

(s/def ::backup-retention-period (spec-or-ref pos-int?))

(def db-cluster-identifier-regex #"^[a-zA-Z][a-zA-Z0-9_\-]*[a-zA-Z0-9_]")
(s/def ::db-cluster-identifier
  (spec-or-ref (s/and string? #(re-matches db-cluster-identifier-regex %))))
(defmethod ->key :db-cluster-identifier [_] "DBClusterIdentifier")

(s/def ::db-cluster-parameter-group-name (spec-or-ref string?))
(defmethod ->key :db-cluster-parameter-group-name [_] "DBClusterParameterGroupName")

(s/def ::db-instance-class (spec-or-ref string?))
(defmethod ->key :db-instance-class [_] "DBInstanceClass")

(s/def ::db-instance-identifier (spec-or-ref string?))
(defmethod ->key :db-instance-identifier [_] "DBInstanceIdentifier")

(s/def ::db-parameter-group-name (spec-or-ref string?))
(defmethod ->key :db-parameter-group-name [_] "DBParameterGroupName")

(s/def ::db-subnet-group-description (spec-or-ref string?))
(defmethod ->key :db-subnet-group-description [_] "DBSubnetGroupDescription")

(s/def ::db-subnet-group-name (spec-or-ref string?))
(defmethod ->key :db-subnet-group-name [_] "DBSubnetGroupName")

(s/def ::description (spec-or-ref string?))

(s/def ::family (spec-or-ref #{"neptune1"}))

(s/def ::iam-auth-enabled (spec-or-ref boolean?))

(s/def ::kms-key-id (spec-or-ref string?))

(s/def ::name (spec-or-ref string?))

(s/def ::parameters (spec-or-ref (s/map-of string? string?)))

(s/def ::port (spec-or-ref pos-int?))

(s/def ::preferred-backup-window (spec-or-ref string?))

(s/def ::preferred-maintenance-window (spec-or-ref string?))

(s/def ::snapshot-identifier (spec-or-ref string?))

(s/def ::storage-encrypted (spec-or-ref boolean?))

(s/def ::subnet-ids (spec-or-ref (s/* string?)))

(s/def ::vpc-security-group-ids (spec-or-ref (s/* string?)))


;;; resource specs

(s/def ::db-cluster
  (s/keys :opt [::availability-zones
                ::backup-retention-period
                ::db-cluster-identifier
                ::db-cluster-parameter-group-name
                ::db-subnet-group-name
                ::iam-auth-enabled
                ::kms-key-id
                ::port
                ::preferred-backup-window
                ::preferred-maintenance-window
                ::snapshot-identifier
                ::storage-encrypted
                :crucible.resources/tags
                ::vpc-security-group-ids]))

(defresource db-cluster "AWS::Neptune::DBCluster" ::db-cluster)

(s/def ::db-cluster-parameter-group
  (s/keys :req [::description
                ::parameters
                ::family]
          :opt [:crucible.resources/tags
                ::name]))

(defresource db-cluster-parameter-group
  "AWS::Neptune::DBClusterParameterGroup"
  ::db-cluster-parameter-group)

(s/def ::db-parameter-group
  (s/keys :req [::description
                ::family]
          :opt [::parameters
                :crucible.resources/tags
                ::name]))

(defresource db-parameter-group "AWS::Neptune::DBParameterGroup" ::db-parameter-group)

(s/def ::db-instance
  (s/keys :req [::db-instance-class]
          :opt [::allow-major-version-upgrade
                ::auto-minor-version-upgrade
                ::availability-zone
                ::db-cluster-identifier
                ::db-instance-identifier
                ::db-parameter-group-name
                ::db-subnet-group-name
                ::preferred-maintenance-window
                :crucible.resources/tags]))

(defresource db-instance "AWS::Neptune::DBInstance" ::db-instance)

(s/def ::db-subnet-group
  (s/keys :req [::db-subnet-group-description
                ::subnet-ids]
          :opt [::db-subnet-group-name
                :crucible.resources/tags]))

(defresource db-subnet-group "AWS::Neptune::DBSubnetGroup" ::db-subnet-group)
