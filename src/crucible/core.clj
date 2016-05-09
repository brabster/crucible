(ns crucible.core
  (:require [crucible.template :as tpl]
            [crucible.resources :as res]))

(defn template
  [& {:as elements}]
  (tpl/make-template elements))

(defn resource
  [spec & properties]
  (apply res/resource spec properties))

(defn xref
  ([r]
   [:ref r])
  ([r att]
   [:ref r att]))

(defn join
  ([v]
   [:fn [:join {:delimiter "" :values v}]])
  ([delimiter v]
   [:fn [:join {:delimiter delimiter :values v}]]))
