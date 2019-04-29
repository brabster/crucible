(ns crucible.policies
  (:require [clojure.spec.alpha :as s]
            [crucible.encoding.keys :refer [->key]]))

(s/def ::min-successful-instances-percent int?)
(s/def ::auto-scaling-creation-policy (s/keys :opt [::min-successful-instances-percent]))

(s/def ::count int?)
(s/def ::timeout string?)
(s/def ::resource-signal (s/keys :opt [::count
                                       ::timeout]))

(s/def ::creation-policy (s/keys :req
                                 [(or ::auto-scaling-creation-policy
                                      ::resource-signal)]))

(s/def ::metadata (s/keys))

(s/def ::will-replace boolean?)
(s/def ::auto-scaling-replacing-update (s/keys :opt [::will-replace]))

(s/def ::max-batch-size int?)
(s/def ::min-instances-in-service int?)
(s/def ::pause-time string?)
(s/def ::suspend-processes (s/* string?))
(s/def ::wait-on-resource-signals boolean?)

(s/def ::auto-scaling-rolling-update (s/keys :opt [::max-batch-size
                                                   ::min-instances-in-service
                                                   ::min-successful-instances-percent
                                                   ::pause-time
                                                   ::suspend-processes
                                                   ::wait-on-resource-signals]))

(s/def ::ignore-unmodified-groups-size-properties boolean?)
(s/def ::auto-scaling-scheduled-action (s/keys :opt [::ignore-unmodified-groups-size-properties]))

(s/def ::update-policy (s/keys :req
                               [(or ::auto-scaling-replacing-update
                                    ::auto-scaling-rolling-update
                                    ::auto-scaling-scheduled-action)]))

(s/def ::deletion-policy #{::retain ::delete ::snapshot})

(s/def ::depends-on keyword?)

(s/def ::condition keyword?)

(s/def ::policy (s/or
                 :deletion-policy ::deletion-policy
                 :depends-on ::depends-on
                 :creation-policy ::creation-policy
                 :update-policy ::update-policy
                 :metadata ::metadata
                 :condition ::condition))

(s/def ::policies (s/keys :opt [::deletion-policy
                                ::depends-on
                                ::creation-policy
                                ::update-policy
                                ::metadata
                                ::condition]))


(defn deletion [policy]
  {::deletion-policy policy})

(defn depends-on [kw]
  {::depends-on kw})

(defn creation-policy [policy]
  {::creation-policy policy})

(defn update-policy [policy]
  {::update-policy policy})

(defn metadata [policy]
  {::metadata policy})

(defn condition [kw]
  {::condition kw})