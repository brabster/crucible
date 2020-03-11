(ns crucible.aws.events.aws-vpc-configuration
  "AWS::ECS::Rule > AwsVpcConfiguration"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::assign-public-ip (spec-or-ref string?))

(s/def ::security-groups (s/coll-of string? :type vector))

(s/def ::subnets (s/coll-of string? :type vector))

(s/def ::aws-vpc-configuration-spec (s/keys :req [::subnets]
                                            :opt [::assign-public-ip
                                                  ::security-groups]))
