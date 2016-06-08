(ns crucible.values
  (:require [camel-snake-kebab.core :refer [->PascalCase]]
            [crucible.template-key :refer [->key]]))

(declare convert-value)

(defn convert-fn-join
  [template spec]
  {"Fn::Join" [(:delimiter spec) (vec (map #(convert-value template %) (:values spec)))]})

(defn convert-fn-select
  [template spec]
  {"Fn::Select" [(:index spec) (vec (map #(convert-value template %) (:values spec)))]})

(defn convert-fn
  [template f]
  (let [type (first f)
        spec (rest f)]
    (cond (= type :join) (apply convert-fn-join template spec)
          (= type :select) (apply convert-fn-select template spec))))

(defn convert-pseudo
  [type]
  (cond (= type :account-id) {"Ref" "AWS::AccountId"}
        (= type :region) {"Ref" "AWS::Region"}
        (= type :notification-arns) {"Ref" "AWS::NotificationARNs"}
        (= type :no-value) {"Ref" "AWS::NoValue"}
        (= type :stack-id) {"Ref" "AWS::StackId"}
        (= type :stack-name) {"Ref" "AWS::StackName"}))

(defn referenceable?
  [template r]
  {:pre [(map? template)]}
  (let [candidates (merge (:parameters template) (:resources template))]
    (contains? candidates r)))

(defn convert-ref
  ([template r]
   {:pre [(referenceable? template r)]}
   {"Ref" (->key r)})
  ([template r att]
   {:pre [(referenceable? template r)]}
   {"Fn::GetAtt" [(->key r) (->key att)]}))

(defn encode-key
  [k]
  (name (->PascalCase k)))

(defn convert-value
  [template v]
  (cond (nil? v) nil
        (string? v) v
        :else (let [type (first v)
                    spec (rest v)]
                (cond (= type :fn) (apply convert-fn template spec)
                      (= type :pseudo) (apply convert-pseudo spec)
                      (= type :ref) (apply convert-ref template spec)
                      :else {(encode-key type) (first spec)}))))

