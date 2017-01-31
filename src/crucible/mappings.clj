(ns crucible.mappings
  (:require [clojure.spec :as s]))

(s/def ::mapping
  (s/map-of string?
            (s/map-of
             string?
             string?)))
