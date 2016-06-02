(ns crucible.template
  (:require [crucible.template-key :refer [->key]]
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

(defmulti place-element (fn [_ _ [type & _]] type))

(defmethod place-element :parameter
  [m k v]
  (assoc-in m ["Parameters" (->key k)] (encode-element m v)))

(defn make-template
  [& {:as elements}]
  (reduce-kv place-element {"AWSTemplateFormatVersion" "2010-09-09"} elements))
