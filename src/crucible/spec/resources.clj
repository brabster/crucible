(ns crucible.spec.resources
  (:require [clojure.spec :as s]
            [crucible.spec.values :as values]
            [crucible.spec.resources.properties :as p]))

(s/def ::name (s/and string? #(re-matches #"([a-zA-Z0-9]+::)+[a-zA-Z0-9]+" %)))

(s/def ::deletion-policy #{::retain ::delete ::snapshot})

(s/def ::depends-on string?)

(s/def ::policies (s/keys :opt [::deletion-policy
                                ::depends-on]))

(s/def ::resource (s/keys :req [::name ::p/properties]
                          :opt [::policies]))

(defn resource [res]
  (let [parsed (s/conform ::resource res)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid resource spec" (s/explain-data ::resource res)))
      parsed)))

(defn resource-factory [name type]
  (if-not (s/valid? ::name name)
    (throw (ex-info "Invalid resource name" (s/explain-data ::name name)))
    (fn [props & [policies]]
      (prn props policies)
      (resource {::name name
                 ::p/properties (assoc props ::p/type type)
                 ::policies policies}))))





