(ns crucible.aws.ecs.cluster
  "Resources in AWS::ECS::Cluster"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::cluster-name (spec-or-ref string?))

(s/def ::cluster (s/keys :req []
                         :opt [::cluster-name]))
