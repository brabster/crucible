(ns crucible.aws.ecs.service
  "Resources in AWS::ECS::Service"
  (:require [clojure.spec.alpha :as s]
            [crucible.encoding.keys :refer [->key]]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::task-definition (spec-or-ref string?))

(s/def ::cluster (spec-or-ref string?))

(s/def ::maximum-percent (spec-or-ref integer?))
(s/def ::minimum-healthy-percent (spec-or-ref integer?))

(s/def ::deployment-configuration (s/keys :req []
                                          :opt [::maximum-percent
                                                ::minimum-healthy-percent]))

(s/def ::desired-count (spec-or-ref integer?))

(s/def ::health-check-grace-period-seconds (spec-or-ref integer?))

(s/def ::launch-type #{"EC2" "FARGATE"})

(s/def ::container-name (spec-or-ref string?))
(s/def ::container-port (spec-or-ref integer?))
(s/def ::load-balancer-name (spec-or-ref string?))
(s/def ::target-group-arn (spec-or-ref string?))

(s/def ::single-load-balancer (s/keys :req []
                                      :opt [::container-name
                                            ::container-port
                                            ::load-balancer-name
                                            ::target-group-arn]))

(s/def ::load-balancers (s/coll-of ::single-load-balancer :kind vector?))


(s/def ::subnets (s/coll-of (spec-or-ref string?) :kind vector?))
(s/def ::assign-public-ip #{"ENABLED" "DISABLED"})
(s/def ::security-groups (s/coll-of (spec-or-ref string?) :kind vector?))


(s/def ::aws-vpc-configuration (s/keys :req [::subnets]
                                       :opt [::assign-public-ip
                                             ::security-groups]))

(defmethod ->key :aws-vpc-configuration [_] "AwsvpcConfiguration")

(s/def ::network-configuration (s/keys :req []
                                       :opt [::aws-vpc-configuration]))

(s/def ::placement-constraint-type #{"distinctInstance" "memberOf"})
(s/def ::placement-constraint-expression (spec-or-ref string?))

(s/def ::placement-constraints (s/keys :req [::placement-constraint-type]
                                       :opt [::placement-constraint-expression]))

(s/def ::placement-strategies-type #{"random" "spread" "binpack"})
(s/def ::placement-strategies-field (spec-or-ref string?))

(s/def ::placement-strategies (s/keys :req [::placement-strategies-type]
                                      :opt [::placement-strategies-field]))

(s/def ::platform-version (spec-or-ref string?))

(s/def ::role (spec-or-ref string?))

(s/def ::service-name (spec-or-ref string?))

(s/def ::service (s/keys :req [::task-definition]
                         :opt [::cluster
                               ::deployment-configuration
                               ::desired-count
                               ::health-check-grace-period-seconds
                               ::launch-type
                               ::load-balancers
                               ::network-configuration
                               ::placement-constraints
                               ::placement-strategies
                               ::platform-version
                               ::role
                               ::service-name]))
