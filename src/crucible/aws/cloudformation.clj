(ns crucible.aws.cloudformation
  (:require [clojure.spec :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :template-url [_] "TemplateURL")

(s/def ::parameters (s/keys))

(s/def ::template-url (spec-or-ref string?))

(s/def ::stack (s/keys ::req [::template-url ::parameters]))

(defresource stack "AWS::CloudFormation::Stack" ::stack)

