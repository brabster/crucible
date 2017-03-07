(ns crucible.resources
  (:require [clojure.spec :as s]
            [crucible.policies :as policies]))

(s/def ::props-type keyword?)

(s/def ::resource-property-value any?)

(defmulti properties-type ::props-type)

(s/def ::generic (s/map-of keyword? ::resource-property-value))

(defmethod properties-type ::generic [_] ::generic)

(s/def ::properties any?)

(s/def ::type (s/and string? #(re-matches #"([a-zA-Z0-9]+::)+[a-zA-Z0-9.]+" %)))

(s/def ::resource (s/keys :req [::type ::properties]
                          :opt [::policies/policies]))

(s/def ::key string?)
(s/def ::value string?)
(s/def ::tag (s/keys :req [::key ::value]))
(s/def ::tags (s/* ::tag))

(defn- assoc-when [m condition k v] (if condition
                                      (assoc m k v)
                                      m))

(def invalid? (complement s/valid?))

(s/def ::policy-list (s/* ::policies/policy))

(defn resource-factory [resource-type props-spec]
  (if-not (s/valid? ::type resource-type)
    (throw (ex-info "Invalid resource name" (s/explain-data ::type resource-type)))
    (fn [& [props & policies]]
      [:resource
       (cond
         (invalid? props-spec props) (throw (ex-info "Invalid resource properties"
                                                     (s/explain-data props-spec props)))

         :else (-> {::type resource-type
                    ::properties props}
                   (merge (into {} (s/conform ::policy-list policies)))))])))

(defmacro spec-or-ref
  "Allows the given spec, keyed as :literal, or a referenced value, keyed as :reference."
  [spec]
  `(s/or :literal ~spec
         :reference :crucible.values/value))

(defmacro defresource
  "Adds a resource factory function to the namespace, documenting the AWS type"
  [sym resource-type props-spec]
  `(def ~(vary-meta
          sym
          assoc :doc (str "CloudFormation Type " resource-type))
     (resource-factory ~resource-type ~props-spec)))
