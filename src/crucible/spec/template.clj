(ns crucible.spec.template
  (:require [clojure.spec :as s]))

(s/def ::description string?)

(s/def ::elements (s/map-of keyword? (s/or :parameter :crucible.spec.parameters/parameter
                                           :resource :crucible.spec.resources/resource)))

(s/def ::template (s/cat :description ::description
                         :elements ::elements))

(defn conform-or-throw [spec input]
  (let [parsed (s/conform spec input)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid input" (s/explain-data spec input)))
      parsed)))

(defn template [description & {:as elements}]
  (conform-or-throw ::template [description elements]))
