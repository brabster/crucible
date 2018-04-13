(ns crucible.core
  "Commonly used template construction functions"
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [crucible.values :as v]
            [crucible.parameters :as p]
            [crucible.resources :as r]
            [crucible.outputs :as o]
            [crucible.encoding :as encoding]
            [expound.alpha :as expound]))

(s/def ::description string?)

(s/def ::element (s/cat :type #{:parameter
                                :condition
                                :mapping
                                :resource
                                :output}
                        :specification any?))

(s/def ::template (s/cat :description ::description
                         :elements (s/nilable (s/map-of keyword? ::element))))

(defn validate
  "Ensure the template is structurally valid, for example xref'd elements exist"
  [template]
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
  ([elements description]
   {:pre [(map? elements)
          (string? description)]}
   (let [input [description elements]
         spec ::template
         parsed (s/conform spec input)]
     (if (= parsed ::s/invalid)
       (throw (ex-info (str "Invalid input" (expound/expound-str spec input))
                       (s/explain-data spec input)))
       (-> parsed
           validate
           (with-meta {::template true})))))
  ([description first-key first-val & {:as  others}]
   (let [elements (assoc others first-key first-val)]
     (template elements description))))

(defn parameter
  "Make a template parameter element"
  [& {:keys [::p/type]
      :or {type ::p/string}
      :as options}]
  [:parameter (assoc options ::p/type type)])

(defn condition
  "Make a template condition element"
  [value]
  [:condition value])

(defn mapping
  "Make a template mapping element"
  [& {:as keymaps}]
  [:mapping keymaps])

(defn resource
  "Make a template resource element"
  [options]
  [:resource options])

(defn output
  "Make a template output with the value and an optional description and export name"
  [value & [description export-name]]
  [:output (-> description
               (when {::o/description description})
               (merge {::o/value value})
               (merge (when export-name {::o/export {::o/name export-name}})))])

(defn xref "Cross-reference another template element, optionally
  specifying a resource attribute. Produces Ref and Fn::GetAtt."
  ([xref]
   (v/xref xref))
  ([xref att]
   (v/xref xref att)))

(defn join
  "Join values at template application time with an optional
  delimiter. See Fn::Join."
  ([values]
   (join "" values))
  ([delimiter values]
   (v/join delimiter values)))

(defn fn-if
  "See Fn:If"
  [condition true-value false-value]
  (v/fn-if condition true-value false-value))

(defn select
  "Select a value from a list at template application time. See
  Fn::Select"
  [index values]
  (v/select index values))

(defn equals
  "See Fn::Equals"
  [x y]
  (v/equals x y))

(defn find-in-map
  "Returns the value corresponding to keys in a two-level map that is declared
   in the Mappings section"
  [map-name top-level-key second-level-key]
  (v/find-in-map map-name top-level-key second-level-key))

(defn import-value
  "Import an exported value"
  [value-name]
  (v/import-value value-name))

(defn sub
  "Interpolate values from a template string"
  [string-to-interpolate]
  (v/sub string-to-interpolate))

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
