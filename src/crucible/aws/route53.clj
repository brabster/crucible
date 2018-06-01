(ns crucible.aws.route53
  "Resources in AWS::Route53::*"
  (:require [crucible.aws.route53.record-set :as record-set]
            [crucible.resources :as res :refer [defresource]]))

(defn prefix [suffix] (str "AWS::Route53::" suffix))

(defresource record-set (prefix "RecordSet") ::record-set/resource-spec)
