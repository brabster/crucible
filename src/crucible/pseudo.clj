(ns crucible.pseudo
  (:require [clojure.spec :as s]))

(s/def ::parameter (s/cat :pseudo #{:pseudo}
                          :type (s/alt ::account-id
                                       ::notification-arns
                                       ::no-value
                                       ::region
                                       ::stack-id
                                       ::stack-name)))

(def account-id [:pseudo ::account-id])
(def notification-arns [:pseudo ::notification-arns])
(def no-value [:pseudo ::no-value])
(def region [:pseudo ::region])
(def stack-id [:pseudo ::stack-id])
(def stack-name [:pseudo ::stack-name])
