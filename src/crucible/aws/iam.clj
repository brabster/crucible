(ns crucible.aws.iam
  "Resources in AWS::IAM::*"
  (:require [crucible.resources :refer [resource-factory spec-or-ref defresource]]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec.alpha :as s]))

(s/def ::assume-role-policy-document any?)

(s/def ::version #{"2012-10-17" "2008-10-17"})
(s/def ::id (spec-or-ref string?))
(s/def ::sid ::id)

(s/def ::single-or-list-string (s/or :single (spec-or-ref string?)
                                     :list (s/+ (spec-or-ref string?))))

(defmethod ->key :aws [_] "AWS")
(s/def ::aws ::single-or-list-string)
(s/def ::aws-account (s/keys :req [::aws]))

(s/def ::federated ::single-or-list-string)
(s/def ::fed (s/keys :req [::federated]))

(s/def ::service ::single-or-list-string)
(s/def ::svc (s/or :kw (s/keys :req [::service])
                   :str (s/map-of string? ::service)))

(s/def ::canonical-user ::single-or-list-string)
(s/def ::canonical (s/keys :req [::canonical-user]))

(s/def ::principal (spec-or-ref (s/or :str string?
                                      :account ::aws-account
                                      :federated ::fed
                                      :service ::svc
                                      :canonical ::canonical)))

(s/def ::not-principal ::principal)

(s/def ::action-element (spec-or-ref (s/and string?
                                            #(re-matches #"[a-zA-Z0-9*:_\-]+" %))))

(s/def ::action (s/or :single ::action-element
                      :list (s/+ ::action-element)))

(s/def ::not-action ::action)

(s/def ::resource-element (spec-or-ref string?))

(s/def ::resource (s/or :single ::resource-element
                        :list (s/+ ::resource-element)))

(s/def ::not-resource ::resource)

;;really complicated and lots of them...
(s/def ::condition (s/map-of (s/or :str string?
                                   :kwd keyword?) (s/map-of string? any?)))

(s/def ::statement (s/* (s/keys :opt [::effect
                                      ::principal
                                      ::not-principal
                                      ::action
                                      ::not-action
                                      ::resource
                                      ::condition])))

(s/def ::policy-document (s/keys :opt [::version
                                       ::id
                                       ::statement
                                       ::sid]))

(s/def ::policy-name (spec-or-ref string?))

(s/def ::groups (spec-or-ref (s/* string?)))

(s/def ::users (spec-or-ref (s/* string?)))

(s/def ::roles (spec-or-ref (s/* string?)))

(s/def ::policy (s/keys :req [::policy-name
                              ::policy-document]
                        :opt [::groups
                              ::roles
                              ::users]))

(s/def ::policies (s/* ::policy))

(s/def ::role (s/keys :req [::assume-role-policy-document
                            ::path
                            ::policies]))

(s/def ::password string?)

(s/def ::password-reset-required boolean?)

(s/def ::login-profile (s/keys :req [::password]
                               :opt [::password-reset-required]))

(s/def ::managed-policy-arns (s/* string?))

(s/def ::username (spec-or-ref string?))

(s/def ::user (s/keys :opt [::groups
                            ::login-profile
                            ::managed-policy-arns
                            ::path
                            ::policies
                            ::username]))

(defresource policy "AWS::IAM::Policy" ::policy)

(defresource user "AWS::IAM::User" ::user)



(defn lambda-access-role
  "Create an AWS::IAM::Role that the Lambda service can assume with
  appropriate CloudWatch log access and the additional access
  statements provided."
  [& statements]
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
