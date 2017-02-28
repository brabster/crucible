(ns crucible-tools
  (:require [cheshire.core :as json]
            [clojure.test :refer :all :as t]
            [clojure.java.io :as io]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [camel-snake-kebab.core :refer [->kebab-case]]))


(def aws-spec (json/decode (slurp "https://d3teyb21fexa9r.cloudfront.net/latest/CloudFormationResourceSpecification.json")))
(def property-types (get aws-spec "PropertyTypes"))
(def resource-types (get aws-spec "ResourceTypes"))
(def sorted-resource-keys (-> resource-types keys sort))
(def sorted-property-keys (-> property-types keys sort))

(defn primitive-type->spec [p]
  (case p
    "String" `(spec-or-ref string?)
    "Number" `(spec-or-ref number?)
    "Integer" `(spec-or-ref integer?)
    "Boolean" `(spec-or-ref boolean?)
    `(spec-or-ref string?)))

(defn non-primitive-type->spec [r i]
   `(spec-or-ref ~(keyword (str r "."  i))))

(defn complex-type->spec [res type coll-item-type]
  (case type
    "List" `(s/coll-of ~coll-item-type :kind vector?)
    "Map" `(s/map-of string? ~coll-item-type)
    `(spec-or-ref ~(keyword (str res "."  type)))))

(defn extract-spec-type [res prim-type type item-type coll-item-type]
  (if prim-type
    (primitive-type->spec prim-type)
    (complex-type->spec res type coll-item-type)))

(defn ->spec [res x {:keys [Documentation PrimitiveType Required
                         UpdateType DuplicatesAllowed PrimitiveItemType Type ItemType] :as d}]
  (let [spec-name (keyword (name res) (name (->kebab-case x)))
        coll-item-type (if PrimitiveItemType (primitive-type->spec PrimitiveItemType) (non-primitive-type->spec res ItemType))
        spec-type (extract-spec-type res PrimitiveType Type ItemType coll-item-type) ]
       {:spec `(s/def ~spec-name ~spec-type) :required Required :type (if PrimitiveType PrimitiveType Type)}))

(defn get-type-properties [r props]
  (let [keyz (keys props)
        data (map #(->spec r %1 (get props %1)) keyz)]
       (vec data)))

(defn kewyord-type [type]
  (keyword type))

(defn extract-properties [p]
  (let [properties (-> (get property-types p) clojure.walk/keywordize-keys :Properties )]
       {:type (kewyord-type p) :properties (get-type-properties p properties)}))

(defn extract-resources [r]
  (let [resource (-> (get resource-types r) clojure.walk/keywordize-keys :Properties )]
       {:type (kewyord-type r) :properties (get-type-properties r resource)}))

(defn parse-resources []
  {:property-types (vec (map extract-properties sorted-property-keys))
  :resource-types (vec (map extract-resources sorted-resource-keys))})

;(parse-resources)