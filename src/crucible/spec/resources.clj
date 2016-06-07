(ns crucible.spec.resources
  (:require [clojure.spec :as s]
            [crucible.spec.values :as v]))

(s/def ::type keyword?)

(s/def ::resource-property-value (s/or :value ::v/value
                                       :values (s/* ::v/value)
                                       :map-vector (s/* map?)))

(defmulti properties-type ::type)

(defmethod properties-type ::generic [_]
  (s/map-of keyword? ::resource-property-value))

(s/def ::properties (s/multi-spec properties-type ::type))

(s/def ::name (s/and string? #(re-matches #"([a-zA-Z0-9]+::)+[a-zA-Z0-9]+" %)))

(s/def ::deletion-policy #{::retain ::delete ::snapshot})

(s/def ::depends-on string?)

(s/def ::policies (s/keys :opt [::deletion-policy
                                ::depends-on]))

(s/def ::resource (s/keys :req [::name ::properties]
                          :opt [::policies]))

(s/def ::key string?)
(s/def ::value string?)
(s/def ::tag (s/keys :req [::key ::value]))
(s/def ::tags (s/* ::tag))

(defn- assoc-when [m test k v] (if test
                                 (assoc m k v)
                                 m))

(defn resource-factory [name type]
  (if-not (s/valid? ::name name)
    (throw (ex-info "Invalid resource name" (s/explain-data ::name name)))
    (fn [& [props policies]]
      (-> {::name name
           ::properties (assoc props ::type type)}
          (assoc-when ((complement nil?) policies) ::policies policies)))))





