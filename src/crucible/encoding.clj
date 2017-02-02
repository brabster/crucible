(ns crucible.encoding
  (:require [clojure.walk :as walk]
            [cheshire.core :as json]
            [crucible.values :as v]
            [crucible.resources :as r]
            [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :aws-template-format-version [_]
  "AWSTemplateFormatVersion")

(defmulti rewrite-element-data
  "Convert the crucible representation of an element into a form ready
  for JSON encoding"
  (fn [[type _]] type))

(defn- unqualify-keyword
  "Remove namespace qualification from a keyword for JSON encoding"
  [kw]
  (-> kw name keyword))

(defn convert-key
  "Prepare key for encoding as JSON"
  [k]
  (-> k unqualify-keyword ->key))

(defmethod rewrite-element-data :default
  [[_ element]]
  (walk/prewalk
   (fn [x]
     (cond
       (::v/type x) (v/encode-value x)
       (keyword? x) (convert-key x)
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
       (keyword? x) (convert-key x)
       :else x))
   element))

(defmethod rewrite-element-data :mapping
  [[_ element]]
  element)

(defn- rewrite-element [[key {:keys [type specification]}]]
  [(->key key) [type (rewrite-element-data [type specification])]])

(defn- element-type->cf-section [element-type]
  (-> element-type
      name
      (str "s")
      keyword
      ->key))

(defn- assemble-template [m [k v]]
  (let [cf-section (element-type->cf-section (first v))
        element-data (second v)]
    (assoc-in m [cf-section k] element-data)))

(defn- elements->template
  "Prepare the elements map for JSON encoding"
  [elements-map empty-template]
  (->> elements-map
       seq
       (map rewrite-element)
       (reduce assemble-template empty-template)))

(defn build
  "Create a CloudFormation-compatible data structure ready for JSON encoding from the template"
  [template]
  (-> template
      :elements
      (elements->template {(->key :aws-template-format-version) "2010-09-09"
                           (->key :description) (or (:description template)
                                                    "No description provided")})))

(defn encode
  "Convert the template data structure into a JSON-encoded string"
  [template]
  (json/encode (build template)))
