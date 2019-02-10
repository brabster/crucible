(ns crucible.aws.serverless
  (:require [crucible.resources :refer [spec-or-ref]]
            [clojure.spec.alpha :as s]))

;; ARN
(s/def ::arn (spec-or-ref string?))

;; Variables
(s/def ::variables (s/map-of (s/or :kw keyword? :str string?) (spec-or-ref string?)))

;; Tags
(s/def ::tags (s/map-of (s/or :kw keyword? :str string?) (spec-or-ref string?)))

;; S3 Location
(s/def ::bucket (spec-or-ref string?))

(s/def ::key (spec-or-ref string?))

(s/def ::version (spec-or-ref int?))

(s/def ::s3-location
  (s/keys :req [::bucket
                ::key]
          :opt [::version]))
;; CORS
(s/def ::allow-methods (spec-or-ref string?))

(s/def ::allow-headers (spec-or-ref string?))

(s/def ::allow-origin (spec-or-ref string?))

(s/def ::max-age (spec-or-ref string?))

(s/def ::cors
  (s/keys :req [::allow-origin]
          :opt [::allow-methods
                ::allow-headers
                ::max-age]))

;; Primary Key
(s/def ::name (spec-or-ref string?))

(s/def ::type (spec-or-ref #{"String" "Number" "Binary"}))

(s/def ::primary-key
  (s/keys :opt [::name
                ::type]))
