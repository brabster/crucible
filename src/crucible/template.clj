(ns crucible.template
  (:require [clojure.spec :as s]
            [crucible.parameters :as p]
            [crucible.resources :as r]
            [crucible.outputs :as o]
            [crucible.encoding :as encoding]))

(s/def ::description string?)

(s/def ::element (s/cat ::type #{:parameter
                                 :resource
                                 :output}
                        ::specification (s/or :parameter ::p/parameter
                                              :resource ::r/resource
                                              :output ::o/output)))

(s/def ::elements (s/map-of keyword? ::element))

(s/def ::template (s/cat :description ::description
                         :elements ::elements))

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

(defn output [& {:keys [value description]}]
  [:output {::o/description description
            ::o/value value}])

(defn xref [& options]
  (vec options))

(defn encode [template]
  (encoding/encode template))
