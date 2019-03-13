(ns crucible.aws.neptune.db-cluster
  "AWS::Neptune::DBCluster"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.neptune :as neptune]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource]]))

(s/def ::availability-zones (spec-or-ref (s/* string?)))

(s/def ::backup-retention-period (spec-or-ref pos-int?))

(s/def ::db-cluster-identifier ::neptune/db-cluster-identifier)

(s/def ::db-cluster-parameter-group-name (spec-or-ref string?))
(defmethod ->key :db-cluster-parameter-group-name [_] "DBClusterParameterGroupName")

(s/def ::db-subnet-group-name ::neptune/db-subnet-group-name)

(s/def ::iam-auth-enabled (spec-or-ref boolean?))

(s/def ::kms-key-id (spec-or-ref string?))

(s/def ::port (spec-or-ref pos-int?))

(s/def ::preferred-backup-window (spec-or-ref string?))

(s/def ::preferred-maintenance-window (spec-or-ref string?))

(s/def ::snapshot-identifier (spec-or-ref string?))

(s/def ::storage-encrypted (spec-or-ref boolean?))

(s/def ::vpc-security-group-ids (spec-or-ref (s/* string?)))

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
