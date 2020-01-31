(ns crucible.aws.ssm
  "Resources in AWS::SSM::*"
  (:require [crucible.resources :refer [defresource]]))

(defn ssm [suffix] (str "AWS::SSM::" suffix))

