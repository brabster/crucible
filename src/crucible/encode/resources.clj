(ns crucible.resources
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.values :refer [convert-value]]))

(defn resource
  [spec & {:as properties}]
  (merge spec properties))

(defn generic-resource
  [resource-type props]
  {:name resource-type
   :properties props})

(defn encode-key
  [k]
  (name (->PascalCase k)))

(defn encode-policy
  [policy]
  (if policy
    (if (map? policy) (reduce-kv (fn [m k v] (assoc m (encode-key k) v)) {} policy)
        (encode-key policy))))

(declare encode-resource-properties)

(defn encode-value
  [template v]
  (cond (map? v) (encode-resource-properties template v)
        (and (vector? v)
             (not (keyword? (first v)))) (vec (map (partial encode-resource-properties template) v))
        :else (convert-value template v)))

(defn encode-resource-properties
  [template properties]
  (if (string? properties)
    properties
    (into {} (map (fn [[k v]] (when v [(encode-key k) (encode-value template v)])) (seq properties)))))

(defn encode-resource
  [template {:keys [name properties creation-policy deletion-policy update-policy depends-on]}]
  (->> {"Type" name
        "CreationPolicy" (encode-policy creation-policy)
        "UpdatePolicy" (encode-policy update-policy)
        "DeletionPolicy" (encode-policy deletion-policy)
        "DependsOn" (convert-value template depends-on)
        "Properties" (encode-resource-properties template properties)}
       seq
       (filter (fn [[k v]] ((complement nil?) v)))
       (into {})))
