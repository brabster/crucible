(ns crucible.outputs
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.values :refer [convert-value]]))

(defn encode-output
  [& {:keys [description value]}]
  (->> {"Description" description
        "Value" (convert-value value)}
       seq
       (filter (fn [[k v]] ((complement nil?) v)))
       (into {})))
