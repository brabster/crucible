(ns crucible.core
  (:require [crucible.values :refer [convert-value]]))

(defn encode
  [map]
  (convert-value map))

