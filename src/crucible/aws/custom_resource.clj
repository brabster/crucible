(ns aws.custom-resource
  (:require [crucible.resources :refer [resource-factory]]
            [crucible.values :as v]
            [crucible.encoding.keys :refer [->key]]
            [clojure.spec :as s]))

(s/def ::service-token ::v/value)

(s/def ::custom-resource (s/keys ::req [::service-token]))

(defn resource [name]
  (resource-factory (str "Custom::" (->key name)) ::custom-resource))
