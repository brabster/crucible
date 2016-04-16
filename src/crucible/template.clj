(ns crucible.template
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.core :as core]
            [crucible.resources :refer [encode-resource]]
            [crucible.parameters :refer [encode-parameter]]))

(defn encode-key
  [k] 
  (name (->PascalCase k)))

(defn encode-template-element
  [encode [k v]]
  [(encode-key k) (encode v)])

(defn make-template
  [element-map]
  (into {"AWSTemplateFormatVersion" "2010-09-09"}
        {"Resources" (into {} (->> element-map
                                   :resources 
                                   seq 
                                   (map (partial encode-template-element encode-resource))))}))
