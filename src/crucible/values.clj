(ns crucible.values
  (:require [clojure.spec :as s]
            [crucible.resources :refer [spec-or-ref]]
            [crucible.encoding.keys :as keys]))

(s/def ::ref keyword?)
(s/def ::att keyword?)
(s/def ::delimiter string?)

(s/def ::param #{::account-id
                 ::notification-arns
                 ::no-value
                 ::region
                 ::stack-id
                 ::stack-name})

(defmulti value-type ::type)

(s/def ::value (s/multi-spec value-type ::type))
(s/def ::values (s/+ ::value))

(s/def ::index (s/and integer?
                      #(>= % 0)))

(s/def ::xref (s/keys :req [::type ::ref]
                      :opt [::att]))

(defmethod value-type ::xref [_] ::xref)

(s/def ::pseudo (s/keys :req [::type ::param]))

(defmethod value-type ::pseudo [_] ::pseudo)

(s/def ::fn-value (spec-or-ref string?))

(s/def ::fn-values (s/coll-of ::fn-value :kind vector?))

(s/def ::join (s/keys :req [::type ::fn-values]
                      :opt [::delimiter]))

(defmethod value-type ::join [_] ::join)

(s/def ::select (s/keys :req [::type ::fn-values ::index]))

(defmethod value-type ::select [_] ::select)




(defmulti encode-value ::type)

(defmethod encode-value :default [x] x)

(defmethod encode-value ::xref [{:keys [::ref ::att]}]
  (if att
    {"Fn::GetAtt" [(keys/->key ref) (keys/->key att)]}
    {"Ref" (keys/->key ref)}))

(defmethod keys/->key :notification-arns [_]
  "NotificationARNs")

(defmethod encode-value ::pseudo [{:keys [::param]}]
  {"Ref" (str "AWS::" (-> param name keyword keys/->key))})

(defmethod encode-value ::join [{:keys [::delimiter ::fn-values]}]
  {"Fn::Join" [(or delimiter "") (vec (map encode-value fn-values))]})

(defmethod encode-value ::select [{:keys [::index ::fn-values]}]
  {"Fn::Select" [(str index) (vec (map encode-value fn-values))]})

(defn xref
  ([ref]
   {::type ::xref ::ref ref})
  ([ref att]
   {::type ::xref ::ref ref ::att att}))

(defn pseudo [param]
  {::type ::pseudo
   ::param param})

(defn join
  [delimiter values]
  {::type ::join
   ::fn-values values
   ::delimiter delimiter})

(defn select [index values]
  {::type ::select
   ::index index
   ::fn-values values})
