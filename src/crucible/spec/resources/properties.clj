(ns crucible.spec.resources.properties
  (:require [clojure.spec :as s]
            [crucible.spec.values :as v]))

(s/def ::type keyword?)

(s/def ::resource-property-value (s/or :value :crucible.spec.values/value
                                       :values (s/* :crucible.spec.values/value)
                                       :map-vector (s/* map?)))

(defmulti properties-type ::type)

(defmethod properties-type ::generic [_]
  (s/map-of keyword? ::resource-property-value))

(s/def ::properties (s/multi-spec properties-type ::type))
