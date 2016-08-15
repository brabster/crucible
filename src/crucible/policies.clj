(ns crucible.policies
  (:require [clojure.spec :as s]
            [crucible.encoding.keys :refer [->key]]))

(s/def ::deletion-policy #{::retain ::delete ::snapshot})

(s/def ::depends-on keyword?)

(s/def ::policy (s/or :deletion-policy ::deletion-policy
                      :depends-on ::depends-on))

(s/def ::policies (s/keys :opt [::deletion-policy
                                ::depends-on]))

(defn deletion [policy]
  policy)

(defn depends-on [kw]
  kw)
