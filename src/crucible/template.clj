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
  (reduce (fn [acc [k v]] (if (seq v) (assoc acc k v) acc))
          {}
          (-> [["AWSTemplateFormatVersion" "2010-09-09"]]
              (conj ["Parameters" (into {} (->> element-map
                                                :parameters
                                                seq
                                                (map (partial encode-template-element encode-parameter))))])
              (conj ["Resources" (into {} (->> element-map
                                               :resources
                                               seq
                                               (map (partial encode-template-element encode-resource))))]))))
