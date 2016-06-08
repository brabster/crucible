(ns crucible.encoding
  (:require [clojure.walk :as walk]
            [camel-snake-kebab.core :refer [->PascalCase]]
            [cheshire.core :as json]))

(defmulti ->key
  "Add methods to override default PascalCase encoding where needed, usually for capitalisation"
  identity)

(defmethod ->key :aws-template-format-version [kw]
  "AWSTemplateFormatVersion")

(defmethod ->key :default [kw]
  (-> kw name ->PascalCase))

(defn validate-element [elements-map element]
  element)

(defmulti rewrite-element-data (fn [[type _]] type))

(defn unqualify-keyword [kw] (-> kw name keyword))

(defmethod rewrite-element-data :default [element]
  (walk/postwalk (fn [x] (if (keyword? x) (unqualify-keyword x) x)) element))

(defn rewrite-element [[key [type data]]]
  [key [type (rewrite-element-data [type data])]])

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
