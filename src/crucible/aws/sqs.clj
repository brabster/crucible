(ns crucible.aws.sqs
  "Resources in AWS::SQS::*"
  (:require [crucible.resources :refer [defresource]]
            [crucible.aws.sqs.queue :as queue]))

(defn prefix [suffix] (str "AWS::SQS::" suffix))

(defresource queue (prefix "Queue") ::queue/resource-spec)
