(ns crucible.resources
  (:require [clojure.spec :as s]
            [crucible.values :as v]))

(s/def ::props-type keyword?)

(s/def ::resource-property-value (s/or :value ::v/value
                                       :values (s/+ ::v/value)
                                       :map-vector (s/+ map?)))

(defmulti properties-type ::props-type)

(defmethod properties-type ::generic [_]
  (s/map-of keyword? ::resource-property-value))

(s/def ::properties (s/multi-spec properties-type ::props-type))

(s/def ::type (s/and string? #(re-matches #"([a-zA-Z0-9]+::)+[a-zA-Z0-9]+" %)))

(s/def ::deletion-policy #{::retain ::delete ::snapshot})

(s/def ::depends-on string?)

(s/def ::policies (s/keys :opt [::deletion-policy
                                ::depends-on]))

(s/def ::resource (s/keys :req [::type ::properties]
                          :opt [::policies]))

(s/def ::key string?)
(s/def ::value string?)
(s/def ::tag (s/keys :req [::key ::value]))
(s/def ::tags (s/* ::tag))

(defn- assoc-when [m test k v] (if test
                                 (assoc m k v)
                                 m))

(defn resource-factory [type props-type]
  (if-not (s/valid? ::type type)
    (throw (ex-info "Invalid resource name" (s/explain-data ::type type)))
    (fn [& [props policies]]
      (-> {::type type
           ::properties (assoc props ::props-type props-type)}
          (assoc-when ((complement nil?) policies) ::policies policies)))))





