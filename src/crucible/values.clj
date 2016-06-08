(ns crucible.values
  (:require [clojure.spec :as s]))

(s/def ::ref keyword?)
(s/def ::att keyword?)

(s/def ::xref (s/cat :ref keyword? :att (s/? keyword?)))

(s/def ::value (s/or :string string? :xref ::xref))
