(ns crucible.aws.rds.db-instance
  "Resources in AWS::RDS::DBInstance"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :size-in-mbs [_] "SizeInMBs")
(defmethod ->key :db-cluster-identifier [_] "DBClusterIdentifier")
(defmethod ->key :db-instance-class [_] "DBInstanceClass")
(defmethod ->key :db-instance-identifier [_] "DBInstanceIdentifier")
(defmethod ->key :db-name [_] "DBName")
(defmethod ->key :db-parameter-group-name [_] "DBParameterGroupName")
(defmethod ->key :db-security-groups [_] "DBSecurityGroups")
(defmethod ->key :db-snapshot-identifier [_] "DBSnapshotIdentifier")
(defmethod ->key :db-snapshot-group-name [_] "DBSubnetGroupName")
(defmethod ->key :domain-iam-role-name [_] "DomainIAMRoleName")
(defmethod ->key :multi-az [_] "MultiAZ")
(defmethod ->key :source-db-instance-identifier [_] "SourceDBInstanceIdentifier")
(defmethod ->key :vpc-security-groups [_] "VPCSecurityGroups")

(s/def ::allocated-storage (spec-or-ref string?))

(s/def ::allow-major-version-upgrade (spec-or-ref string?))

(s/def ::auto-minor-version-upgrade (spec-or-ref string?))

(s/def ::availability-zone (spec-or-ref string?))

(s/def ::backup-retention-period (spec-or-ref string?))

(s/def ::character-set-name (spec-or-ref string?))

(s/def ::copy-tags-to-snapshot (spec-or-ref boolean?))

(s/def ::db-cluster-identifier (spec-or-ref string?))

(s/def ::db-instance-class (spec-or-ref string?))

(s/def ::db-instance-identifier (spec-or-ref string?))

(s/def ::db-name (spec-or-ref string?))

(s/def ::db-parameter-group-name (spec-or-ref string?))

(s/def ::db-security-groups (s/* (spec-or-ref string?)))

(s/def ::db-snapshot-identifier (spec-or-ref string?))

(s/def ::db-snapshot-group-name (spec-or-ref string?))

(s/def ::domain (spec-or-ref string?))

(s/def ::domain-iam-role-name (spec-or-ref string?))

(s/def ::engine (spec-or-ref string?))

(s/def ::engine-version (spec-or-ref string?))

(s/def ::iops (spec-or-ref number?))

(s/def ::kms-key-id (spec-or-ref string?))

(s/def ::license-model (spec-or-ref string?))

(s/def ::master-username (spec-or-ref string?))

(s/def ::master-username-password (spec-or-ref string?))

(s/def ::monitoring-interval (spec-or-ref string?))

(s/def ::monitoring-role-arn (spec-or-ref string?))

(s/def ::multi-az (spec-or-ref boolean?))

(s/def ::option-group-name (spec-or-ref string?))

(s/def ::port (spec-or-ref string?))

(s/def ::preferred-backup-window (spec-or-ref string?))

(s/def ::preferred-maintenance-window (spec-or-ref string?))

(s/def ::publicly-accessible (spec-or-ref boolean?))

(s/def ::source-db-instance-identifier (spec-or-ref string?))

(s/def ::storage-encrypted (spec-or-ref boolean?))

(s/def ::storage-type (spec-or-ref string?))

(s/def ::tags (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::timezone (spec-or-ref string?))

(s/def ::vpc-security-groups (s/coll-of (spec-or-ref string?) :kind vector?))
