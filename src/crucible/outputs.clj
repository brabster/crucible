(ns crucible.outputs
  (:require [clojure.spec :as s]
            [crucible.values :as v]))

(s/def ::description string?)

(s/def ::value ::v/value)

(s/def ::output (s/keys :req [::value]
                        :opt [::description]))
