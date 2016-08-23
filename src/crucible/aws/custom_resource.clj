(ns crucible.aws.custom-resource
  "AWS Custom Resources have a type that is defined by the client,
  prefixed with 'Custom::'. Clients must therefore specify this name
  when using the resource."
  (:require [crucible.resources :refer [spec-or-ref resource-factory]]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(s/def ::service-token (spec-or-ref string?))

(defn resource
  "Define an AWS Custom::ResourceName named for the resource-name
  argument. Optionally pass parameters directly, for example where the
  definition will not be reused."
  ([resource-name parameters]
   ((resource resource-name) parameters))
  ([resource-name]
   (resource-factory (str "Custom::" (->key resource-name)) (s/keys ::req [::service-token]))))
