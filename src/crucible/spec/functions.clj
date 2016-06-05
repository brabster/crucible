(ns crucible.spec.functions
  (:require [clojure.spec :as s]
            [crucible.spec.values :as values]))

(defmulti function-type :fn/type)

(s/def ::delimiter string?)
(s/def ::values (s/* :value/value))

(s/def ::index (s/and pos? integer?))

(defmethod function-type :fn/join [_]
  (s/keys :req [:fn/values]
          :opt [:fn/delimiter]))

(defmethod function-type :fn/select [_]
  (s/keys :req [:fn/values :fn/index]))

(s/def :fn/fn (s/multi-spec function-type :fn/type))
