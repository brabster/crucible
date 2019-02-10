(ns crucible.encoding.serverless
  (:require [crucible.encoding.keys :refer [->key]]
            [crucible.encoding :as encoding]
            [cheshire.core :as json]))

(defn build
  "Create a Serverless Application Model compatible data structure ready for
  JSON encoding from the template and any global template values"
  ([template]
   (build template nil))
  ([template globals]
   (-> template
       :elements
       (#'encoding/elements->template
        (cond-> {(->key :aws-template-format-version) "2010-09-09"
                 (->key :description) (or (:description template)
                                          "No description provided")
                 (->key :transform) "AWS::Serverless-2016-10-31"}
          globals (assoc (->key :globals)
                         (encoding/rewrite-element-data globals)))))))

(defn encode
  "Convert the template data structure into a JSON-encoded string"
  ([template]
   (encode template nil))
  ([template globals]
   (json/encode (build template globals))))
