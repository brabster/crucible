(ns crucible.aws.serverless.function.event-source
  (:require [crucible.aws.serverless :as sam]
            [crucible.aws.serverless.function.event-source.api :as api]
            [crucible.aws.serverless.function.event-source.kinesis :as kinesis]
            [crucible.aws.serverless.function.event-source.schedule :as schedule]
            [crucible.aws.serverless.function.event-source.sns :as sns]
            [crucible.aws.serverless.function.event-source.sqs :as sqs]
            [crucible.resources :refer [spec-or-ref defresource]]
            [clojure.spec.alpha :as s]))

(s/def ::type #{"AlexaSkill"
                "Api"
                "CloudWatchEvent"
                "CloudWatchLogs"
                "DynamoDB"
                "IoTRule"
                "Kinesis"
                "S3"
                "Schedule"
                "SNS"
                "SQS"})

(defmulti event-source ::type)

(defmethod event-source "Kinesis" [_]
  (s/keys :req [::type
                ::kinesis/properties]))

(defmethod event-source "Schedule" [_]
  (s/keys :req [::type
                ::schedule/properties]))

(defmethod event-source "Api" [_]
  (s/keys :req [::type
                ::api/properties]))

(defmethod event-source "SNS" [_]
  (s/keys :req [::type
                ::sns/properties]))

(defmethod event-source "SQS" [_]
  (s/keys :req [::type
                ::sqs/properties]))

(defmethod event-source :default [_]
  (s/keys :req [::type]))

(s/def ::event-source
  (s/multi-spec event-source ::type))
