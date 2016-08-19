(ns crucible.aws.iam
  (:require [crucible.resources :refer [resource-factory spec-or-ref]]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(s/def ::assume-role-policy-document any?)

(s/def ::statement (s/* (s/keys :opt [::effect
                                      ::principal
                                      ::action
                                      ::resource
                                      ::condition])))

(s/def ::policy-document (s/keys :opt [::version
                                       ::statement]))

(s/def ::policy-name (spec-or-ref string?))

(s/def ::policy (s/keys :req [::policy-name
                              ::policy-document]))

(s/def ::policies (s/* ::policy))

(s/def ::role (s/keys :req [::assume-role-policy-document
                            ::path
                            ::policies]))

(defn lambda-access-role [& statements]
  ((resource-factory "AWS::IAM::Role" ::role)
   {::assume-role-policy-document {::version "2012-10-17"
                                   ::statement [{::effect "Allow"
                                                 ::principal {::service ["lambda.amazonaws.com"]}
                                                 ::action ["sts:AssumeRole"]}]}
    ::path "/"
    ::policies [{::policy-name "root"
                 ::policy-document {::version "2012-10-17"
                                    ::statement (conj statements
                                                      {::action ["logs:CreateLogGroup"
                                                                 "logs:CreateLogStream"
                                                                 "logs:PutLogEvents"]
                                                       ::effect "Allow"
                                                       ::resource "*"})}}]}))
