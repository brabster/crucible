(ns crucible.aws.ecr
  "Resources in AWS::ECR::*"
  (:require [crucible.aws.ecr.repository :as repository]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(defn prefix [suffix] (str "AWS::ECR::" suffix))

(defresource repository (prefix "Repository") ::repository/resource-spec)
