(ns crucible.encoding
  (:require [clojure.walk :as walk]
            [cheshire.core :as json]
            [crucible.values :as v]
            [crucible.resources :as r]
            [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :aws-template-format-version [_]
  "AWSTemplateFormatVersion")

(defn validate-element [_ element]
  element)

(defmulti rewrite-element-data (fn [[type _]] type))

(defn unqualify-keyword [kw] (-> kw name keyword))

(defmethod rewrite-element-data :default
  [[_ element]]
  (walk/prewalk
   (fn [x]
     (cond
       (::v/type x) (v/encode-value x)
       (keyword? x) (-> x unqualify-keyword ->key)
       :else x))
   element))

(defmethod rewrite-element-data :resource
  [[_ element]]
  (walk/prewalk
   (fn [x]
     (cond
       (::r/policies x) (-> x
                            (merge (::r/policies x))
                            (dissoc ::r/policies))
       (::v/type x) (v/encode-value x)
       (keyword? x) (-> x unqualify-keyword ->key)
       :else x))
   element))

(defn rewrite-element [[key {:keys [type specification]}]]
  [key [type (rewrite-element-data [type specification])]])

(defn element-type->cf-section [type]
  (-> type
      name
      (str "s")
      keyword))

(defn assemble-template [m [k v]]
  (let [cf-section (element-type->cf-section (first v))
        element-data (second v)]
    (assoc-in m [cf-section k] element-data)))

(defn elements->template [elements-map empty-template]
  (->> elements-map
       seq
       (map (partial validate-element elements-map))
       (map rewrite-element)
       (reduce assemble-template empty-template)))

(defn encode [template]
  (-> template
      :elements
      (elements->template {:aws-template-format-version "2010-09-09"
                           :description (or (:description template) "No description provided")})
      (json/encode {:key-fn ->key})))
