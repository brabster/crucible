(ns crucible.template
  "Commonly used template construction functions"
  (:require [clojure.spec :as s]
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

(defn template "Make a template structure with the given description and elements"
  [description & {:as elements}]
  (let [input [description elements]
        spec ::template
        parsed (s/conform spec input)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid input" (s/explain-data spec input)))
      parsed)))

(defn parameter "Make a template parameter element"
  [& {:keys [type] :or {type ::p/string} :as options}]
  [:parameter (assoc options ::p/type type)])

(defn resource "Make a template resource element"
  [options]
  [:resource options])

(defn output "Make a template output with the value and an optional description"
  [value & [description]]
  [:output (-> description
               (when {::o/description description})
               (assoc ::o/value value))])

(defn xref "Cross-reference another template element"
  [& options]
  (apply v/xref options))

(defn encode "Encode a template into JSON for use by CloudFormation"
  [template]
  (encoding/encode template))
