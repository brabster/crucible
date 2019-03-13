(ns crucible.aws.neptune.db-instance
  "Specs for AWS::Neptune::DBInstance"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.neptune :as neptune]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource]]))

(s/def ::allow-major-version-upgrade (spec-or-ref boolean?))

(s/def ::auto-minor-version-upgrade (spec-or-ref boolean?))

(s/def ::availability-zone (spec-or-ref string?))

(s/def ::db-cluster-identifier ::neptune/db-cluster-identifier)

(s/def ::db-instance-class (spec-or-ref string?))
(defmethod ->key :db-instance-class [_] "DBInstanceClass")

(s/def ::db-instance-identifier (spec-or-ref string?))
(defmethod ->key :db-instance-identifier [_] "DBInstanceIdentifier")

(s/def ::db-parameter-group-name (spec-or-ref string?))
(defmethod ->key :db-parameter-group-name [_] "DBParameterGroupName")

(s/def ::db-subnet-group-name ::neptune/db-subnet-group-name)

(s/def ::preferred-maintenance-window (spec-or-ref string?))

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
