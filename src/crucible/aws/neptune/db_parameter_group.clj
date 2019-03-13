(ns crucible.aws.neptune.db-parameter-group
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.neptune :as neptune]
            [crucible.resources :refer [spec-or-ref defresource]]))

(s/def ::description ::neptune/description)

(s/def ::family ::neptune/family)

(s/def ::parameters ::neptune/parameters)

(s/def ::name ::neptune/name)

(s/def ::db-parameter-group
  (s/keys :req [::description
                ::family]
          :opt [::parameters
                :crucible.resources/tags
                ::name]))

(defresource db-parameter-group "AWS::Neptune::DBParameterGroup" ::db-parameter-group)

