(ns crucible.aws.neptune.db-subnet-group
  "Specs for AWS::Neptune::DBSubnetGroup"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.neptune :as neptune]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref defresource]]))

(s/def ::db-subnet-group-description (spec-or-ref string?))
(defmethod ->key :db-subnet-group-description [_] "DBSubnetGroupDescription")

(s/def ::db-subnet-group-name ::neptune/db-subnet-group-name)

(s/def ::subnet-ids (spec-or-ref (s/* string?)))

(s/def ::db-subnet-group
  (s/keys :req [::db-subnet-group-description
                ::subnet-ids]
          :opt [::db-subnet-group-name
                :crucible.resources/tags]))

(defresource db-subnet-group "AWS::Neptune::DBSubnetGroup" ::db-subnet-group)
