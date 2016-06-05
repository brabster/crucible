(ns crucible.spec.values
  (:require [clojure.spec :as s]))

(s/def ::xref keyword?)

(s/def ::value (s/or :string string? :ref ::xref))
