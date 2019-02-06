(ns crucible.aws.serverless.api
  (:require [crucible.aws.serverless :as sam]
            [crucible.resources :refer [spec-or-ref defresource]]
            [clojure.spec.alpha :as s]))

(s/def ::name (spec-or-ref string?))

(s/def ::stage-name (spec-or-ref string?))

(s/def ::definition-uri (spec-or-ref (s/or :string string?
                                           :s3-location ::sam/s3-location)))

(s/def ::definition-body map?)

(s/def ::cache-clustering-enabled (spec-or-ref boolean?))

(s/def ::cache-cluster-size (spec-or-ref string?))

(s/def ::variables ::sam/variables)

(s/def ::http-method (spec-or-ref string?))

(s/def ::resource-path (spec-or-ref string?))

(s/def ::method-setting
  (s/keys :req [::http-method
                ::resource-path]))

(s/def ::method-settings (s/* ::method-settings))

(s/def ::endpoint-configuration #{"REGIONAL" "EDGE" "PRIVATE"})

(s/def ::binary-media-types (s/* (spec-or-ref string?)))

(s/def ::cors (spec-or-ref (s/or :domain string?
                                 :cors-configuration ::sam/cors)))

(s/def ::api
  (s/keys :req [::stage-name]
          :opt [::name
                ::definition-uri
                ::definition-body
                ::cache-clustering-enabled
                ::cache-cluster-size
                ::variables
                ::method-settings
                ::endpoint-configuration
                ::binary-media-types
                ::cors]))

(defresource api "AWS::Serverless::Api" ::api)
