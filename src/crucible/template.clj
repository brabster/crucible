(ns crucible.template
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.core :as core]
            [crucible.resources :refer [encode-resource]]
            [crucible.parameters :refer [encode-parameter]]
            [crucible.outputs :refer [encode-output]]))

(defn- encode-key
  [k]
  (name (->PascalCase k)))

(defn- encode-template-element
  [encode [k v]]
  [(encode-key k) (encode v)])

(defn- encode-elements
  [template-map type-label encode-type-fn]
  [(encode-key type-label)
   (into {} (->> template-map
                 type-label
                 seq
                 (map (partial encode-template-element encode-type-fn))))])

(defn make-template
  [element-map]
  (reduce (fn [acc [k v]] (if (seq v) (assoc acc k v) acc))
          {}
          [["AWSTemplateFormatVersion" "2010-09-09"]
           (encode-elements element-map :parameters encode-parameter)
           (encode-elements element-map :resources encode-resource)
           (encode-elements element-map :outputs encode-output)]))
