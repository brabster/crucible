(ns cloudforge.core
  (:require [cloudforge.values :refer [convert-value]]))

(defn encode
  [map]
  (convert-value map))

