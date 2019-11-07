(ns crucible.aws.resource-attributes
  "Resource attributes as specified in https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-product-attribute-reference.html"
  (:require [clojure.spec.alpha :as s]))

(s/def ::depends-on keyword?)

