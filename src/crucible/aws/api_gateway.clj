(ns crucible.aws.api-gateway
  "Resources in AWS::ApiGateway::*"
  (:require [clojure.spec :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.encoding.keys :refer [->key]]))

(defn apigw [resource] (str "AWS::ApiGateway::" (->key resource)))

(s/def ::rest-api (s/keys :opt [::body
                                ::body-s3-location
                                ::clone-from
                                ::description
                                ::fail-on-warnings
                                ::name
                                ::parameters]))

(s/def ::account any?)

(defresource account (apigw :account) ::account)

(s/def ::api-key any?)

(defresource api-key (apigw :api-key) ::api-key)

(s/def ::authorizer any?)

(defresource authorizer (apigw :authorizer) ::authorizer)

(s/def ::base-path-mapping any?)

(defresource base-path-mapping (apigw :base-path-napping) ::base-path-mapping)

(s/def ::client-certificate any?)

(defresource client-certificate (apigw :client-certificate) ::client-certificate)

(s/def ::deployment any?)

(defresource deployment (apigw :deployment) ::deployment)

(s/def ::method any?)

(defresource method (apigw :method) ::method)

(s/def ::model any?)

(defresource model (apigw :model) ::model)

(s/def ::resource any?)

(defresource resource (apigw :resource) ::resource)

(s/def ::rest-api any?)

(defresource rest-api (apigw :rest-api) ::rest-api)

(s/def ::stage any?)

(defresource stage (apigw :stage) ::stage)

(s/def ::usage-plan any?)

(defresource usage-plan (apigw :usage-plan) ::usage-plan)