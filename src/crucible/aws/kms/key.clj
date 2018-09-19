(ns crucible.aws.kms.key
  "AWS::KMS::Key"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref] :as res]))

(s/def ::description (spec-or-ref string?))
(s/def ::enabled (spec-or-ref boolean?))
(s/def ::enable-key-rotation (spec-or-ref boolean?))
(s/def ::key-policy (spec-or-ref any?))

(s/def ::resource-spec (s/keys :req [::key-policy]
                               :opt [::description
                                     ::enabled
                                     ::enable-key-rotation
                                     ::res/tags]))
