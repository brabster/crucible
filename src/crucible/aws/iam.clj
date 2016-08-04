(ns crucible.aws.iam
  (:require [crucible.resources :refer [resource-factory]]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(s/def ::assume-role-policy-document any?)

(defn lambda-access-role [& statements]
  ((resource-factory "AWS::IAM::Role" ::assume-role-policy-document)
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
