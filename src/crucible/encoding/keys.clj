(ns crucible.encoding.keys
  (:require [camel-snake-kebab.core :refer [->PascalCase]]))

(defmulti ->key
  "Add methods to override default PascalCase encoding where needed, usually for capitalisation"
  identity)

(defmethod ->key :default [kw]
  (-> kw name ->PascalCase))
