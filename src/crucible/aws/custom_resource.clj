(ns crucible.aws.custom-resource
  "AWS Custom Resources have a type that is defined by the client,
  prefixed with 'Custom::'. Clients must therefore specify this name
  when using the resource."
  (:require [crucible.resources :refer [resource-factory spec-or-ref]]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(s/def ::service-token (spec-or-ref string?))

(defn resource
  "Define a custom resource named for the argument, optionally pass parameters directly"
  ([name parameters]
   ((resource name) parameters))
  ([name]
   (resource-factory (str "Custom::" (->key name)) (s/keys ::req [::service-token]))))
