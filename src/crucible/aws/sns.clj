(ns crucible.aws.sns
  "Resources in AWS::SNS::*"
  (:require [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.iam :as iam]
            [clojure.spec.alpha :as s]))

(s/def ::display-name (spec-or-ref string?))

(s/def ::topic-name (spec-or-ref string?))

(s/def ::endpoint string?)

(s/def ::protocol #{"http" "https" "email" "email-json" "sms" "sqs" "application" "lambda"})

(s/def ::subscription (s/* (s/keys :req [::endpoint
                                         ::protocol])))

(s/def ::topic (s/keys :opt [::display-name
                             ::topic-name
                             ::subscription]))

(defresource topic "AWS::SNS::Topic" ::topic)

(s/def ::topics (s/* (spec-or-ref string?)))

(s/def ::topic-policy (s/keys :req [::iam/policy-document
                                    ::topics]))

(defresource topic-policy "AWS::SNS::TopicPolicy" ::topic-policy)
