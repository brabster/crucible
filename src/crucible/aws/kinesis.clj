(ns crucible.aws.kinesis
  (require [clojure.spec :as s]
           [crucible.resources :refer [spec-or-ref resource-factory]]))

(s/def ::shard-count (spec-or-ref pos-int?))

(s/def ::name (spec-or-ref string?))

(s/def ::stream (s/keys :req [::shard-count]
                        :opt [::name
                              :crucible.resourcs/tags]))

(def stream (resource-factory "AWS::Kinesis::Stream" ::stream))

