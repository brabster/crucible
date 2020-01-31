(ns crucible.aws.rds.db-subnet-group
  "AWS::RDS::DBSubnetGroup"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref] :as res]
            [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :db-subnet-group-name [_] "DBSubnetGroupName")
(defmethod ->key :db-subnet-group-description [_] "DBSubnetGroupDescription")

(s/def ::db-subnet-group-name (spec-or-ref string?))

(s/def ::db-subnet-group-description (spec-or-ref string?))

(s/def ::subnet-ids (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::db-subnet-group-spec (s/keys :req [::db-subnet-group-description
                                            ::subnet-ids]
                                      :opt [::db-subnet-group-name
                                            ::res/tags]))
