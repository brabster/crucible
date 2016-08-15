(ns crucible.policies
  (:require [clojure.spec :as s]))

(s/def ::deletion-policy #{::retain ::delete ::snapshot})

(s/def ::depends-on string?)

(s/def ::policies (s/nilable (s/keys :opt [::deletion-policy
                                           ::depends-on])))
