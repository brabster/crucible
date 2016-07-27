(ns crucible.resources
  (:require [clojure.spec :as s]
            [crucible.values :as v]))

(s/def ::props-type keyword?)

(s/def ::resource-property-value (s/or :value ::v/value
                                       :values ::v/values
                                       :map-vector (s/+ map?)))

(defmulti properties-type ::props-type)

(s/def ::generic (s/map-of keyword? ::resource-property-value))

(defmethod properties-type ::generic [_] ::generic)

(s/def ::properties any?)

(s/def ::type (s/and string? #(re-matches #"([a-zA-Z0-9]+::)+[a-zA-Z0-9]+" %)))

(s/def ::deletion-policy #{::retain ::delete ::snapshot})

(s/def ::depends-on string?)

(s/def ::policies (s/nilable (s/keys :opt [::deletion-policy
                                           ::depends-on])))

(s/def ::resource (s/keys :req [::type ::properties]
                          :opt [::policies]))

(s/def ::key string?)
(s/def ::value string?)
(s/def ::tag (s/keys :req [::key ::value]))
(s/def ::tags (s/* ::tag))

(defn- assoc-when [m test k v] (if test
                                 (assoc m k v)
                                 m))

(def invalid? (complement s/valid?))

(defn resource-factory [type props-spec]
  (if-not (s/valid? ::type type)
    (throw (ex-info "Invalid resource name" (s/explain-data ::type type)))
    (fn [& [props policies]]
      [:resource
       (cond
         (invalid? props-spec props) (throw (ex-info "Invalid resource properties"
                                                     (s/explain-data props-spec props)))
         (invalid? ::policies policies) (throw (ex-info "Invalid resource policies"
                                                        (s/explain-data ::policies policies)))
         :else (-> {::type type
                    ::properties props}
                   (assoc-when ((complement nil?) policies) ::policies policies)))])))
