(ns crucible.aws.auto-scaling.launch-configuration
  "AWS::AutoScaling::LaunchConfiguration"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref] :as res]))

(s/def ::associate-public-ip-address (spec-or-ref boolean?))
(s/def ::iam-instance-profile (spec-or-ref string?))
(s/def ::image-id (spec-or-ref string?))
(s/def ::instance-id (spec-or-ref string?))
(s/def ::instance-monitoring (spec-or-ref boolean?))
(s/def ::instance-type (spec-or-ref string?))
(s/def ::kernel-id (spec-or-ref string?))
(s/def ::key-name (spec-or-ref string?))
(s/def ::security-group (spec-or-ref string?))
(s/def ::security-groups (s/* ::security-group))
(s/def ::user-data (spec-or-ref string?))

(s/def ::resource-spec (s/keys :req [::image-id
                                            ::instance-type]
                                      :opt [::associate-public-ip-address
                                            ::iam-instance-profile
                                            ::instance-id
                                            ::instance-monitoring
                                            ::kernel-id
                                            ::key-name
                                            ::security-groups
                                            ::user-data]))
