(ns crucible.functions
  (:require [clojure.spec :as s]
            [crucible.values :as v]))

(defmulti function-type ::type)

(s/def ::fn (s/multi-spec function-type ::type))

(s/def ::delimiter string?)
(s/def ::values (s/+ ::v/value))

(s/def ::index (s/and pos? integer?))

(defn join
  ([values]
   (join "" values))
  ([delimiter values]
   {::type ::join
    ::values values
    ::delimiter delimiter}))

(defmethod function-type ::join [_]
  (s/keys :req [::values]
          :opt [::delimiter]))

(defn select [index values]
  {::type ::select
   ::index index
   ::values values})

(defmethod function-type ::select [_]
  (s/keys :req [::values ::index]))
