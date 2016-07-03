(ns crucible.parameters
  (:require [clojure.spec :as s]))

(s/def ::type #{::string ::number})

(s/def ::description string?)

(s/def ::constraint-description string?)

(s/def ::allowed-values (s/+ string?))

(s/def ::allowed-pattern (partial instance? java.util.regex.Pattern))

(s/def ::default string?)

(s/def ::no-echo #{true})

(s/def ::max-value (s/and number? pos?))

(s/def ::min-value ::max-value)

(s/def ::max-length (s/and integer? pos?))

(s/def ::min-length ::max-length)

(s/def ::parameter
  (s/keys :req [::type]
          :opt [::description
                ::allowed-values
                ::allowed-pattern
                ::constraint-description
                ::default
                ::no-echo
                ::min-value
                ::max-value
                ::min-length
                ::max-length]))
