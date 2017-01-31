(ns crucible.mappings
  (:require [clojure.spec :as s]))

(s/def ::name
  string?)

(d/def ::value
  string?)

(s/def ::keymap
  (s/map-of ::name
            ::value))

(s/def ::mapping
  (s/map-of string?
            ::keymap))

(s/def ::mappings
  (s/map-of string?
            ::mapping))
