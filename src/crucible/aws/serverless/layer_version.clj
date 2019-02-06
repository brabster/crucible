(ns crucible.aws.serverless.layer-version
  (:require [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.serverless :as sam]
            [clojure.spec.alpha :as s]))

(s/def ::layer-name (spec-or-ref string?))

(s/def ::description (spec-or-ref string?))

(s/def ::content-uri (spec-or-ref (s/or :string string?
                                        :s3-location ::sam/s3-location)))

(s/def ::compatible-runtimes (spec-or-ref (s/* string?)))

(s/def ::license-info (spec-or-ref string?))

(s/def ::retention-policy (spec-or-ref #{"Retain" "Delete"}))

(s/def ::layer-version
  (s/keys :req [::content-uri]
          :opt [::layer-name
                ::description
                ::compatible-runtimes
                ::license-info
                ::retention-policy]))

(defresource layer-version "AWS::Serverless::LayerVersion" ::layer-version)
