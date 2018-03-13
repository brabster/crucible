(ns crucible.mappings
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :as res :refer [defresource spec-or-ref]]))

(s/def ::mapping
  (s/map-of (spec-or-ref string?)
            (s/map-of
             (spec-or-ref string?)
             (spec-or-ref string?))))
