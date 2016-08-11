(ns crucible.aws.kinesis
  (require [clojure.spec :as s]
           [crucible.values :as v]
           [crucible.resources :as r]
           [crucible.template :as t]))

(s/def ::shard-count (v/spec-or-ref pos-int?))

(s/def ::name (v/spec-or-ref string?))

(s/def ::stream (s/keys :req [::shard-count]
                        :opt [::name
                              ::v/tags]))

(def stream (r/resource-factory "AWS::Kinesis::Stream" ::stream))

