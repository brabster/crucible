(ns crucible.aws.serverless.globals
  (:require [crucible.aws.serverless.api :as sam.api]
            [crucible.aws.serverless.function :as sam.function]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]))

(s/def ::function (s/keys :opt [::sam.function/handler
                                ::sam.function/runtime
                                ::sam.function/code-uri
                                ::sam.function/description
                                ::sam.function/memory-size
                                ::sam.function/timeout
                                ::sam.function/vpc-config
                                ::sam.function/environment
                                ::sam.function/tags]))

(s/def ::api (s/keys :opt [::sam.api/name
                           ::sam.api/definition-uri
                           ::sam.api/cache-clustering-enabled
                           ::sam.api/cache-cluster-size
                           ::sam.api/variables
                           ::sam.api/endpoint-configuration
                           ::sam.api/method-settings
                           ::sam.api/binary-media-types
                           ::sam.api/cors]))

(s/def ::globals (s/keys :opt [::function
                               ::api]))

(defn globals
  "Validates Serverless Application Model globals returning a vector of
  :globals and the conformed data"
  [props]
  (if-not (s/valid? ::globals props)
    (throw (ex-info (str "Invalid globals property"
                         (expound/expound-str ::globals props))
                    (s/explain-data ::globals props)))
    [:globals props]))
