(ns crucible.template
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
                        :specification ::s/any))

(s/def ::template (s/cat :description ::description
                         :elements (s/nilable (s/map-of keyword? ::element))))

(defn conform-or-throw [spec input]
  (let [parsed (s/conform spec input)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid input" (s/explain-data spec input)))
      parsed)))

(defn template [description & {:as elements}]
  (conform-or-throw ::template [description elements]))

(defn parameter [& {:keys [type]
                    :or {type ::p/string}
                    :as options}] 
  [:parameter (assoc options ::p/type type)])

(defn resource [{:as options}]
  [:resource options])

(defn output 
  [value & [description]]
  [:output (-> description
               (when {::o/description description})
               (assoc ::o/value value))])

(defn xref [& options]
  (apply v/xref options))

(defn encode [template]
  (encoding/encode template))
