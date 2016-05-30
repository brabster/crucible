(ns crucible.resources.aws.iam
  (:require [crucible.resources :refer [generic-resource]]))

(defn role
  [& {:keys [assume-role-policy-document managed-policy-arns path policies] :as props}]
  (generic-resource "AWS::IAM::Role" props))

(defn policy-document [& statements])

(defn statement [& {:keys [effect actions resources principal sid]
                  :or {effect "Allow"}}]
  {:sid sid
   :effect effect
   :principal principal
   :action actions
   :resource resources})

(defn assume-role-policy-document [principal]
  {:version "2012-10-17"
   :statement [(statement :principal principal :actions ["sts:AssumeRole"])]})

(defn policy [policy-name & statements]
  {:version "2012-10-17"
   :statement (vec statements)})
