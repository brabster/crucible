(ns crucible.core
  "Commonly used template construction functions"
  (:require [clojure.spec :as s]
            [clojure.walk :as walk]
            [crucible.values :as v]
            [crucible.parameters :as p]
            [crucible.resources :as r]
            [crucible.outputs :as o]
            [crucible.encoding :as encoding]))

(s/def ::description string?)

(s/def ::element (s/cat :type #{:parameter
                                :resource
                                :output}
                        :specification any?))

(s/def ::template (s/cat :description ::description
                         :elements (s/nilable (s/map-of keyword? ::element))))

(defn validate [template]
  (walk/prewalk
   (fn [x]
     (cond
       (and (vector? x)
            (= 2 (count x))
            (= ::v/ref (first x))) (if-not (contains? (:elements template) (second x))
                                     (throw (ex-info "Missing reference" {:ref (second x)}))
                                     x)
       :else x))
   template))

(defn template
  "Make a template structure with the given description and elements"
  [description & {:as elements}]
  (let [input [description elements]
        spec ::template
        parsed (s/conform spec input)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid input" (s/explain-data spec input)))
      (validate parsed))))

(defn parameter
  "Make a template parameter element"
  [& {:keys [::p/type]
      :or {type ::p/string}
      :as options}]
  [:parameter (assoc options ::p/type type)])

(defn resource
  "Make a template resource element"
  [options]
  [:resource options])

(defn output
  "Make a template output with the value and an optional description"
  [value & [description]]
  [:output (-> description
               (when {::o/description description})
               (assoc ::o/value value))])

(defn xref "Cross-reference another template element, optionally
  specifying a resource attribute. Produces Ref and Fn::GetAtt."
  ([ref]
   (v/xref ref))
  ([ref att]
   (v/xref ref att)))

(defn join
  "Join values at template application time with an optional
  delimiter. See Fn::Join."
  ([values]
   (join "" values))
  ([delimiter values]
   (v/join delimiter values)))

(defn select
  "Select a value from a list at template application time. See
  Fn::Select"
  [index values]
  (v/select index values))

(defn encode
  "Encode a template into JSON for use by CloudFormation"
  [template]
  (encoding/encode template))

(def account-id (v/pseudo ::v/account-id))
(def notification-arns (v/pseudo ::v/notification-arns))
(def no-value (v/pseudo ::v/no-value))
(def region (v/pseudo ::v/region))
(def stack-id (v/pseudo ::v/stack-id))
(def stack-name (v/pseudo ::v/stack-name))
