(ns crucible.aws.serverless.function
  (:require [crucible.aws.iam :as iam]
            [crucible.aws.lambda :as lambda]
            [crucible.aws.serverless :as sam]
            [crucible.aws.serverless.function.event-source :as event-source]
            [crucible.resources :refer [spec-or-ref defresource]]
            [clojure.spec.alpha :as s]))

(s/def ::handler (spec-or-ref string?))

(s/def ::runtime (spec-or-ref string?))

(s/def ::code-uri
  (spec-or-ref
   (s/or :string string?
         :s3-location ::sam/s3-location)))

(s/def ::inline-code (spec-or-ref string?))

(s/def ::function-name (spec-or-ref (s/and string? #(<= (count %) 64))))

(s/def ::description (spec-or-ref string?))

(s/def ::memory-size (spec-or-ref int?))

(s/def ::timeout (spec-or-ref int?))

(s/def ::role ::sam/arn)

(s/def ::policies (s/or :name string?
                        :policy ::iam/policy-document
                        :list (s/* (s/or :name string?
                                         :policy ::iam/policy-document))))

(s/def ::events (spec-or-ref (s/map-of (s/or :string string? :keyword keyword?)
                                       ::event-source/event-source)))

(s/def ::vpc-config ::lambda/vpc-config)

(s/def ::variables ::sam/variables)

(s/def ::environment (s/keys :req [::variables]))

(s/def ::tags ::sam/tags)

(s/def ::function
  (s/keys :opt [::handler ; required but could be in globals
                ::runtime ; required but could be in globals
                ;; either code-uri or inline-code are required but either could
                ;; be defined in globals
                ::code-uri
                ::inline-code
                ::function-name
                ::description
                ::memory-size
                ::timeout
                ::role
                ::policies
                ::environment
                ::vpc-config
                ::tags]))

(defresource function "AWS::Serverless::Function" ::function)
