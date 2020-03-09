(ns crucible.aws.events.network-configuration
  "AWS::ECS::Rule > NetworkConfiguration"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.events.aws-vpc-configuration :as aws-vpc-configuration]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::aws-vpc-configuration ::aws-vpc-configuration/aws-vpc-configuration-spec)

(s/def ::network-configuration-spec (s/keys :opt [::aws-vpc-configuration]))
