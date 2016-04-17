(ns crucible.resources.aws.dynamo-db
  (:require [crucible.resources :refer [generic-resource]]))

(defn table
  [props]
  (generic-resource "AWS::DynamoDB::Table" props))
