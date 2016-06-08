(ns crucible.outputs
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.values :refer [convert-value]]))

(defn encode-output
  ([template value]
   (encode-output template value nil))
  ([template value description]
   (->> {"Description" description
         "Value" (convert-value template value)}
        seq
        (filter (fn [[k v]] ((complement nil?) v)))
        (into {}))))
