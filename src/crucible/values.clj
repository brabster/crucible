(ns crucible.values
  (:require [clojure.spec :as s]
            [crucible.encoding.keys :as keys]
            [crucible.pseudo :as pseudo]))

(s/def ::ref keyword?)
(s/def ::att keyword?)
(s/def ::delimiter string?)
(s/def ::values (s/+ ::value))
(s/def ::index (s/and pos? integer?))

(defmulti value-type (fn [x] (or (::type x) (class x))))

(s/def ::xref (s/keys :req [::type ::ref]
                      :opt [::att]))

(s/def ::value (s/multi-spec value-type ::type))

(defmethod value-type java.lang.String [_] string?)

(defmethod value-type ::xref [_] ::xref)

(s/def ::pseudo (s/keys :req [::type ::param]))

(defmethod value-type ::pseudo [_] ::pseudo)

(s/def ::join (s/keys :req [::type ::values]
                      :opt [::delimiter]))

(defmethod value-type ::join [_] ::join)

(s/def ::select (s/keys :req [::type ::values ::index]))

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

(defmethod encode-value ::join [{:keys [::delimiter ::values]}]
  {"Fn::Join" [(or delimiter "") (vec (map encode-value values))]})

(defmethod encode-value ::select [{:keys [::index ::values]}]
  {"Fn::Select" [(str index) (vec (map encode-value values))]})




(defn xref
  ([ref]
   {::type ::xref ::ref ref})
  ([ref att]
   {::type ::xref ::ref ref ::att att}))

(defn pseudo [param]
  {::type ::pseudo
   ::param (keyword "crucible.pseudo" (name param))})

(defn join
  ([values]
   (join "" values))
  ([delimiter values]
   {::type ::join
    ::values values
    ::delimiter delimiter}))

(defn select [index values]
  {::type ::select
   ::index index
   ::values values})
