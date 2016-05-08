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

(defmethod encode-kv :constraint-description
  [[k constraint-description]]
  [(encode-key k) (str constraint-description)])

(defmethod encode-kv :allowed-pattern
  [[k pattern]]
  [(encode-key k) (str pattern)])

(defmethod encode-kv :min-value
  [[k n]]
  [(encode-key k) (str n)])

(defmethod encode-kv :max-value
  [[k n]]
  [(encode-key k) (str n)])

(defmethod encode-kv :min-length
  [[k n]]
  [(encode-key k) (str n)])

(defmethod encode-kv :max-length
  [[k n]]
  [(encode-key k) (str n)])

(defn encode-parameter
  [_ spec]
  (into {} (map encode-kv (seq spec))))
