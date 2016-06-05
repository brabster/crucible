(ns crucible.template.cloudformation
  (:require [clojure.spec :as s]))

(defmulti function-type :fn/type)

(s/def ::xref keyword?)
(s/def ::value (s/alt :string string? :ref ::xref))
(s/def ::delimiter string?)
(defmethod function-type :fn/join [_]
  (s/keys :req [:fn/values]
          :opt [:fn/delimiter]))

(s/def ::index (s/and pos? integer?))
(defmethod function-type :fn/select [_]
  (s/keys :req [:fn/values :fn/index]))

(s/def :fn/fn (s/multi-spec function-type :fn/type))







(defmulti value-type :value/type)

(s/def :value/value (s/multi-spec value-type :value/type))

(s/def ::element-key keyword?)
(s/def ::resource (s/map-of ::element-key ::value))

(s/def ::aws-template-format-version #{"2010-09-09"})
(s/def ::resources (s/map-of ::element-key ::resource))

(s/def ::template (s/keys :req [::aws-template-format-version
                                ::resources]
                          :opt [::description
                                ::metadata
                                ::mappings
                                ::parameters
                                ::outputs]))
