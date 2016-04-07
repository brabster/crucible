(ns crucible.parameters
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.values :refer [convert-value]]))

(defn encode-key
  [k]
  (name (->PascalCase k)))

(defmulti encode-kv (fn [[k _]] k))

(defmethod encode-kv :type
  [[k type]]
  [(encode-key k) (name (->PascalCase type))])

(defmethod encode-kv :description
  [[k desc]]
  [(encode-key k) (str desc)])

(defmethod encode-kv :allowed-values
  [[k allowed-values]]
  [(encode-key k) allowed-values])

(defmethod encode-kv :default-value
  [[k default-value]]
  [(encode-key :default) (str default-value)])

(defmethod encode-kv :no-echo
  [[k no-echo?]]
  [(encode-key k) (str no-echo?)])

(defn encode-parameter
  [spec]
  (into {} (map encode-kv (seq spec))))
