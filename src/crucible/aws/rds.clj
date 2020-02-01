(ns crucible.aws.rds
  "Resources in AWS::RDS::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.encoding.keys :refer [->key]]
            [crucible.aws.rds.db-subnet-group :as db-subnet-group]
            [crucible.aws.rds.db-instance :as dbi]))

(defn rds [resource] (str "AWS::RDS::" (->key resource)))

(defmethod ->key :db-cluster [_] "DBCluster")
(defmethod ->key :db-instance [_] "DBInstance")
(defmethod ->key :db-security-group [_] "DBSecurityGroup")
(defmethod ->key :db-security-group-ingress [_] "DBSecurityGroupIngress")
(defmethod ->key :db-subnet-group [_] "DBSubnetGroup")

(s/def ::db-instance (s/keys :opt [::dbi/allocated-storage
                                   ::dbi/allow-major-version-upgrade
                                   ::dbi/auto-minor-version-upgrade
                                   ::dbi/availability-zone
                                   ::dbi/backup-retention-period
                                   ::dbi/character-set-name
                                   ::dbi/copy-tags-to-snapshot
                                   ::dbi/db-cluster-identifier
                                   ::dbi/db-instance-class
                                   ::dbi/db-instance-identifier
                                   ::dbi/db-name
                                   ::dbi/db-parameter-group-name
                                   ::dbi/db-security-groups
                                   ::dbi/db-snapshot-identifier
                                   ::dbi/db-snapshot-group-name
                                   ::dbi/domain
                                   ::dbi/domain-iam-role-name
                                   ::dbi/engine
                                   ::dbi/engine-version
                                   ::dbi/iops
                                   ::dbi/kms-key-id
                                   ::dbi/license-model
                                   ::dbi/master-username
                                   ::dbi/master-username-password
                                   ::dbi/monitoring-interval
                                   ::dbi/monitoring-role-arn
                                   ::dbi/multi-az
                                   ::dbi/option-group-name
                                   ::dbi/port
                                   ::dbi/preferred-backup-window
                                   ::dbi/preferred-maintenance-window
                                   ::dbi/publicly-accessible
                                   ::dbi/source-db-instance-identifier
                                   ::dbi/storage-encrypted
                                   ::dbi/storage-type
                                   ::dbi/tags
                                   ::dbi/timezone
                                   ::dbi/vpc-security-groups]))

(defresource db-instance (rds :db-instance) ::db-instance)

(s/def ::db-cluster any?)
(defresource db-cluster (rds :db-cluster) ::db-cluster)

(s/def ::db-security-group any?)
(defresource db-security-group (rds :db-security-group) ::db-security-group)

(s/def ::db-security-group-ingress any?)
(defresource db-security-group-ingress (rds :db-security-group-ingress) ::db-security-group-ingress)

(defresource db-subnet-group (rds :db-subnet-group) ::db-subnet-group/db-subnet-group-spec)

(s/def ::event-subscription any?)
(defresource event-subscription (rds :event-subscription) ::event-subscription)

(s/def ::option-group any?)
(defresource option-group (rds :option-group) ::option-group)
