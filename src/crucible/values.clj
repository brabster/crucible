(ns crucible.values
  (:require [clojure.spec.alpha :as s]
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

(s/def ::condition-name (spec-or-ref string?))

(s/def ::value-if-true (spec-or-ref string?))

(s/def ::value-if-false (spec-or-ref string?))

(s/def ::if (s/keys :req [::type ::condition-name ::value-if-true ::value-if-false]))

(defmethod value-type ::if [_] ::if)

(s/def ::select (s/keys :req [::type ::fn-values ::index]))

(defmethod value-type ::select [_] ::select)

(s/def ::map-name (spec-or-ref keyword?))

(s/def ::top-level-key (spec-or-ref string?))

(s/def ::second-level-key (spec-or-ref string?))

(s/def ::find-in-map (s/keys :req [::type
                                   ::map-name
                                   ::top-level-key
                                   ::second-level-key]))

(defmethod value-type ::find-in-map [_] ::find-in-map)



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

(defmethod encode-value ::if [{::keys [condition-name value-if-true value-if-false]}]
  {"Fn::If" [condition-name value-if-true value-if-false]})

(defmethod encode-value ::select [{:keys [::index ::fn-values]}]
  {"Fn::Select" [(str index) (vec (map encode-value fn-values))]})

(defmethod encode-value ::equals [{:keys [::x ::y]}]
  {"Fn::Equals" [x y]})

(defmethod encode-value ::find-in-map [{:keys [::map-name
                                               ::top-level-key
                                               ::second-level-key]}]
  {"Fn::FindInMap"
   [(keys/->key map-name)
    (encode-value top-level-key)
    (encode-value second-level-key)]})

(defn xref
  ([xref]
   {::type ::xref ::ref xref})
  ([xref att]
   {::type ::xref ::ref xref ::att att}))

(defn pseudo [param]
  {::type ::pseudo
   ::param param})

(defn join
  [delimiter values]
  {::type ::join
   ::fn-values values
   ::delimiter delimiter})

(defn fn-if
  [condition-name value-if-true value-if-false]
  {::type ::if
   ::condition-name condition-name
   ::value-if-true value-if-true
   ::value-if-false value-if-false})

(defn select [index values]
  {::type ::select
   ::index index
   ::fn-values values})

(defn equals [x y]
  {::type ::equals
   ::x x
   ::y y})

(defn find-in-map [map-name top-level-key second-level-key]
  {::type ::find-in-map
   ::map-name map-name
   ::top-level-key top-level-key
   ::second-level-key second-level-key})

(s/def ::value-name (spec-or-ref string?))
(s/def ::import-value (s/keys :req [::value-name]))

(defmethod value-type ::import-value [_] ::import-value)

(defn import-value [value-name]
  {::type ::import-value
   ::value-name value-name})

(s/def ::sub-literal (spec-or-ref string?))
(s/def ::sub (s/keys :req [::sub-literal]))

(defmethod value-type ::sub [_] ::sub)

(defn sub [string-to-interpolate]
  {::type ::sub
   ::sub-literal string-to-interpolate})
