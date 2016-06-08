(ns crucible.template
  (:require [schema.core :as schema]
            [clojure.spec :as spec]
            [crucible.template-key :refer [->key]]
            [crucible.resources :refer [encode-resource]]
            [crucible.parameters :refer [encode-parameter]]
            [crucible.outputs :refer [encode-output]]))



(defmulti encode-element (fn [_ [type & _]] type))

(defmethod encode-element :default
  [template-map [type spec]]
  (prn type spec))

(defmethod encode-element :parameter
  [template-map [_ spec]]
  (encode-parameter nil spec))

(s/defn ^:always-validate element-type->cf-type :- OutputTemplateSectionKey
  [type :- InputTemplateElementType]
  (-> type
      name
      (str "s")
      ->key))

(defn place-encoded-element
  [m k [type & _ :as v]]
  (if (= k :description) (assoc m "Description" v)
      (assoc-in m [(element-type->cf-type type) (->key k)] (encode-element m v))))

(s/defn make-template
  [& element-seq] :- OutputTemplate
  (let [{:as elements} element-seq]
    (reduce-kv place-encoded-element
               {"AWSTemplateFormatVersion" ""}
               elements)))
