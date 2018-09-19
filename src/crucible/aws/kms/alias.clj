(ns crucible.aws.kms.alias
  "AWS::KMS::Key"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref] :as res]))

(s/def ::alias-name (spec-or-ref string?))
(s/def ::target-key-id (spec-or-ref string?))
(s/def ::resource-spec (s/keys :req [::alias-name
                                     ::target-key-id]))
