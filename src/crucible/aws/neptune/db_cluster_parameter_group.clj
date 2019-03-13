(ns crucible.aws.neptune.db-cluster-parameter-group
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.neptune :as neptune]
            [crucible.resources :refer [spec-or-ref defresource]]))

(s/def ::description ::neptune/description)

(s/def ::family ::neptune/family)

(s/def ::parameters ::neptune/parameters)

(s/def ::name ::neptune/name)

(s/def ::db-cluster-parameter-group
  (s/keys :req [::description
                ::parameters
                ::family]
          :opt [:crucible.resources/tags
                ::name]))

(defresource db-cluster-parameter-group
  "AWS::Neptune::DBClusterParameterGroup"
  ::db-cluster-parameter-group)
