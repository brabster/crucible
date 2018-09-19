(ns crucible.aws.kms
  "Resources in AWS::KMS::*"
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.aws.kms.key :as key]
            [crucible.aws.kms.alias :as alias]))

(defn prefix [suffix] (str "AWS::KMS::" suffix))

(defresource key (prefix "Key") ::key/resource-spec)

(defresource alias (prefix "Alias") ::alias/resource-spec)
