(ns crucible.aws.cloudformation
  (:require [clojure.spec :as s]
            [crucible.resources :refer [resource-factory]]
            [crucible.encoding.keys :refer [->key]]
            [crucible.values :refer [spec-or-ref]]))

(defmethod ->key :template-url [_] "TemplateURL")

(s/def ::parameters (s/keys))

(s/def ::template-url (spec-or-ref string?))

(s/def ::stack (s/keys ::req [::template-url ::parameters]))

(def stack (resource-factory "AWS::CloudFormation::Stack" ::stack))

