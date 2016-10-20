(ns crucible.aws.sns
  "Resources in AWS::SNS::*"
  (:require [crucible.resources :refer [spec-or-ref defresource]]
            [clojure.spec :as s]))

(s/def ::display-name (spec-or-ref string?))

(s/def ::topic-name (spec-or-ref string?))

(s/def ::topic (s/keys :opt [::display-name
                             ::topic-name]))

(defresource topic "AWS::SNS::Topic" ::topic)
