(ns crucible.template-key
  (:require [camel-snake-kebab.core :refer [->PascalCase]]))

(defn ->key
  [k]
  (name (->PascalCase k)))
