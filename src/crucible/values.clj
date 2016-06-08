(ns crucible.values
  (:require [clojure.spec :as s]
            [crucible.pseudo :as pseudo]))

(s/def ::ref keyword?)
(s/def ::att keyword?)

(defmulti function-type ::type)

(s/def ::fn (s/multi-spec function-type ::type))

(s/def ::xref (s/cat :ref keyword? :att (s/? keyword?)))

(s/def ::value (s/or :string string?
                     :xref ::xref
                     :pseudo ::pseudo/parameter
                     :function ::fn))


(s/def ::delimiter string?)
(s/def ::values (s/+ ::value))

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
