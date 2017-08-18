(ns crucible.outputs
  (:require [clojure.spec.alpha :as s]
            [crucible.values :as v]))

(s/def ::name ::v/value)

(s/def ::export (s/keys :req [::name]))

(s/def ::description string?)

(s/def ::value ::v/value)

(s/def ::output (s/keys :req [::value]
                        :opt [::description]))
