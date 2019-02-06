(ns crucible.aws.serverless.function.event-source.api
  (:require [crucible.resources :refer [spec-or-ref]]
            [clojure.spec.alpha :as s]))

(s/def ::path (spec-or-ref string?))

(s/def ::method (spec-or-ref string?))

(s/def ::rest-api-id (spec-or-ref string?))

(s/def ::properties
  (s/keys :req [::path
                ::method]))
