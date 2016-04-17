(ns crucible.resources
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.values :refer [convert-value]]))

(defn generic-resource
  [resource-type props]
  {:name resource-type
   :properties props})

(defn encode-key
  [k]
  (name (->PascalCase k)))

(defn encode-policy
  [policy]
  (if policy (encode-key policy)))

(declare encode-resource-properties)

(defn encode-value
  [v]
  (cond (map? v) (encode-resource-properties v)
        (and (vector? v)
             (not (keyword? (first v)))) (into [] (map encode-resource-properties v))
        :else (convert-value v)))

(defn encode-resource-properties
  [properties]
  (into {} (map (fn [[k v]] [(encode-key k) (encode-value v)]) (seq properties))))

(defn encode-resource
  [type-spec & {:keys [creation-policy deletion-policy update-policy depends-on properties]}]
  (->> {"Type" (:name type-spec)
        "CreationPolicy" (encode-policy creation-policy)
        "UpdatePolicy" (encode-policy update-policy)
        "DeletionPolicy" (encode-policy deletion-policy)
        "DependsOn" (convert-value depends-on)
        "Properties" (encode-resource-properties (:properties type-spec))}
       seq
       (filter (fn [[k v]] ((complement nil?) v)))
       (into {})))
