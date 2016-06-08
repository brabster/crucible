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
  ([vals]
   [:fn [:join {:delimiter "" :values vals}]])
  ([delimiter vals]
   [:fn [:join {:delimiter delimiter :values vals}]]))

(defn select
  [i vals]
  [:fn [:select {:index i :values vals}]])


