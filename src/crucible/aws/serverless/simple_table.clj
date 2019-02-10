(ns crucible.aws.serverless.simple-table
  (:require [crucible.aws.serverless :as sam]
            [crucible.aws.dynamodb :as ddb]
            [crucible.resources :refer [spec-or-ref defresource]]
            [clojure.spec.alpha :as s]))

(s/def ::primary-key (spec-or-ref ::sam/primary-key))

(s/def ::provisioned-throughput ::ddb/provisioned-throughput)

(s/def ::tags ::sam/tags)

(s/def ::table-name (spec-or-ref string?))

(s/def ::sse-specification ::ddb/sse-specification)

(s/def ::simple-table
  (s/keys :opt [::primary-key
                ::provisioned-throughput
                ::tags
                ::table-name
                ::sse-specification]))

(defresource simple-table "AWS::Serverless::SimpleTable" ::simple-table)
