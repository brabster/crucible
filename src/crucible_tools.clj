(ns crucible-tools
  (:require [cheshire.core :as json]
            [clojure.test :refer [is are testing] :as t]
            [clojure.java.io :as io]
            [crucible.resources :refer [spec-or-ref defresource] :as res]
            [crucible.core :as cc]
            [camel-snake-kebab.core :refer [->kebab-case]]
            [clojure.spec :as s]
            [clojure.string :as st]
            [crucible.encoding.keys :as enc]))

(def fix-typos-in-spec {["AWS.RDS.DBSecurityGroup.Ingress" "CDIRIP"] ["AWS.RDS.DBSecurityGroup.Ingress" "CIDRIP"]})

;;TODO should things like Enabled be in it's own primitive ns as a boolean, rather than creating loads of different ones?
(t/with-test
  (defn ->spec-name [prefix resource-type resource-name]
    (let [l (st/split resource-type #"::")
          ns-part (st/join "." (butlast l))
          el-part (last l)
          [el-part1 el-part2] (st/split el-part #"\.")
          [ns-part el-part] (if el-part2
                              [(str ns-part "." el-part1) el-part2]
                              [ns-part el-part1])
          ns (str ns-part
                  (when (and (not-empty el-part) (not-empty resource-name))
                    (str "." el-part)))
          [ns resource-name] (fix-typos-in-spec [ns resource-name] [ns resource-name])
          ns (str prefix (when (and prefix (not-empty ns-part)) ".") ns)
          el (if (empty? resource-name)
               el-part
               resource-name)]
      (if (empty? el)
        (keyword prefix resource-type)
        (keyword ns el))))

  (testing "->spec-name"
    (is (= :crucible.generated.AWS.ElasticLoadBalancing.LoadBalancer/AccessLoggingPolicy
           (->spec-name "crucible.generated" "AWS::ElasticLoadBalancing::LoadBalancer.AccessLoggingPolicy" nil)))
    (is (= :crucible.generated.AWS.ElasticLoadBalancing.LoadBalancer.AccessLoggingPolicy/EmitInterval
           (->spec-name "crucible.generated" "AWS::ElasticLoadBalancing::LoadBalancer.AccessLoggingPolicy" "EmitInterval")))
    (is (= :crucible.generated.AWS.Lambda/EventSourceMapping
           (->spec-name "crucible.generated" "AWS::Lambda::EventSourceMapping" nil)))
    (is (= :crucible.generated.Tag/Key
           (->spec-name "crucible.generated" "Tag" "Key")))
    (is (= :crucible.generated/Tag
           (->spec-name "crucible.generated" "Tag" nil)))
    (is (= :crucible.generated.AWS.RDS.DBSecurityGroup.Ingress/CIDRIP
           (->spec-name "crucible.generated" "AWS::RDS::DBSecurityGroup.Ingress" "CDIRIP")))))

(defn primitive-type->spec [p]
  (case p
    "String"  `(spec-or-ref string?)
    "Number"  `(spec-or-ref number?)
    "Integer" `(spec-or-ref integer?)
    "Boolean" `(spec-or-ref boolean?)
    `(spec-or-ref string?)))

(defn non-primitive-type->spec [prefix r i]
  `(spec-or-ref ~(->spec-name prefix r i)))

(defn complex-type->spec [res type coll-item-type]
  (case type
    "List" `(s/coll-of ~coll-item-type :kind vector?)
    "Map"  `(s/map-of string? ~coll-item-type)
    nil))

(defn extract-spec-type [res prim-type type item-type coll-item-type]
  (if prim-type
    (primitive-type->spec prim-type)
    (complex-type->spec res type coll-item-type)))

(defn ->spec [res x prefix {:strs [Documentation PrimitiveType Required UpdateType DuplicatesAllowed PrimitiveItemType Type ItemType] :as d}]
  (let [spec-name      (->spec-name prefix res x)
        coll-item-type (if PrimitiveItemType
                         (primitive-type->spec PrimitiveItemType)
                         (non-primitive-type->spec prefix res ItemType))
        spec-type      (when coll-item-type
                         (extract-spec-type res PrimitiveType Type ItemType coll-item-type))]
    (when spec-type
      `[(s/def ~spec-name ~spec-type)
        (defmethod enc/->key ~(keyword (name spec-name)) [~'_] ~x)])))

(defn get-type-properties [r prefix props]
  (mapcat #(->spec r %1 prefix (get props %1)) (keys props)))

(defn ->spec-keys [^clojure.lang.Keyword n ks]
  (->> ks
       (keep first)
       sort
       (mapv #(keyword (str (namespace n) "." (name n)) %))))

(defn extract-properties [p prefix properties]
  (let [{required true optional false} (group-by #(get (val %) "Required") properties)
        n                              (->spec-name prefix p nil)
        required                       (->spec-keys n required)
        optional                       (->spec-keys n optional)
        element-properties             (into required optional)
        constructor-args               (mapv (comp symbol ->kebab-case name) element-properties)
        arg-sym                        (gensym "arg")]
    `[(ns ~(symbol (namespace n)))
      (defn ~(symbol (str "->" (name n)))
        ~(str "Constructor for a " n)
        [ & ~(conj constructor-args :as arg-sym)]
        (let [m# (zipmap ~element-properties (take (count ~arg-sym) ~constructor-args))
              f# (if (.contains ~p ".")
                   identity
                   (res/resource-factory ~p ~n))]
          (if (s/valid? ~n m#)
            (f# m#)
            (throw (ex-info (str "Not a valid " ~n) (s/explain-data ~n m#))))))
      (defn ~(symbol (str "map->" (name n)))
        ~(str "Convert a map to a " n)
        ~[{:keys constructor-args :as arg-sym}]
        (let [m# (zipmap (map #(keyword (str (namespace ~n) "." (name ~n)) (name %)) (keys ~arg-sym)) (vals ~arg-sym))
              f# (if (.contains ~p ".")
                   identity
                   (res/resource-factory ~p ~n))]
          (if (s/valid? ~n m#)
            (f# m#)
            (throw (ex-info (str "Not a valid " ~n) (s/explain-data ~n m#))))))
      (ns ~(symbol (str (namespace n) "." (name n))))
      (s/def ~n (s/keys :req ~required :opt ~optional))
      (defresource ~(symbol (name n)) ~p ~n)]))

(defn extract-resources [prefix [p v]]
  (when-not (= p "Tag")
    (let [properties (get v "Properties")]
      (concat (get-type-properties p prefix properties)
              (extract-properties p prefix properties)))))

(defn load-from-source [file resource]
  (or (try
        (io/reader (io/resource resource))
        (catch Exception e nil))
      (io/reader file)))

(defn parse-resources [prefix {:keys [region file resource]}]
  (let [aws-spec (json/decode-stream (load-from-source file resource))
        prefix (str prefix "." region)]
    (->> (concat (get aws-spec "PropertyTypes") (get aws-spec "ResourceTypes"))
         (mapcat (partial extract-resources prefix))
         (remove nil?))))

(def region-specs [{:name     "Asia Pacific (Mumbai) Region"
                    :region   "ap-south-1"
                    :file     "resources/ap-south-1.json"
                    :resource "ap-south-1.json"
                    :url      "https://d2senuesg1djtx.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "Asia Pacific (Seoul) Region"
                    :region   "ap-northeast-2"
                    :file     "resources/ap-northeast-2.json"
                    :resource "ap-northeast-2.json"
                    :url      "https://d1ane3fvebulky.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "Asia Pacific (Sydney) Region"
                    :region   "ap-southeast-2"
                    :file     "resources/ap-southeast-2.json"
                    :resource "ap-southeast-2.json"
                    :url      "https://d2stg8d246z9di.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "Asia Pacific (Singapore) Region"
                    :region   "ap-southeast-1"
                    :file     "resources/ap-southeast-1.json"
                    :resource "ap-southeast-1.json"
                    :url      "https://doigdx0kgq9el.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "Asia Pacific (Tokyo) Region"
                    :region   "ap-northeast-1"
                    :file     "resources/ap-northeast-1.json"
                    :resource "ap-northeast-1.json"
                    :url      "https://d33vqc0rt9ld30.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "Canada (Central) Region"
                    :region   "ca-central-1"
                    :file     "resources/ca-central-1.json"
                    :resource "ca-central-1.json"
                    :url      "https://d2s8ygphhesbe7.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "EU (Frankfurt) Region"
                    :region   "eu-central-1"
                    :file     "resources/eu-central-1.json"
                    :resource "eu-central-1.json"
                    :url      "https://d1mta8qj7i28i2.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "EU (London) Region"
                    :region   "eu-west-2"
                    :file     "resources/eu-west-2.json"
                    :resource "eu-west-2.json"
                    :url      "https://d1742qcu2c1ncx.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "EU (Ireland) Region"
                    :region   "eu-west-1"
                    :file     "resources/eu-west-1.json"
                    :resource "eu-west-1.json"
                    :url      "https://d3teyb21fexa9r.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "South America (São Paulo)"
                    :region   "sa-east-1"
                    :file     "resources/sa-east-1.json"
                    :resource "sa-east-1.json"
                    :url      "https://d3c9jyj3w509b0.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "US East (N. Virginia)"
                    :region   "us-east-1"
                    :file     "resources/us-east-1.json"
                    :resource "us-east-1.json"
                    :url      "https://d1uauaxba7bl26.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "US East (Ohio)"
                    :region   "us-east-2"
                    :file     "resources/us-east-2.json"
                    :resource "us-east-2.json"
                    :url      "https://dnwj8swjjbsbt.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "US West (N. California)"
                    :region   "us-west-1"
                    :file     "resources/us-west-1.json"
                    :resource "us-west-1.json"
                    :url      "https://d68hl49wbnanq.cloudfront.net/latest/CloudFormationResourceSpecification.json"}
                   {:name     "US West (Oregon)"
                    :region   "us-west-2"
                    :file     "resources/us-west-2.json"
                    :resource "us-west-2.json"
                    :url      "https://d201a2mn26r7lk.cloudfront.net/latest/CloudFormationResourceSpecification.json"}])

(defn fetch-specs []
  (doseq [{:keys [url file]} region-specs]
    (spit file (slurp url))))

(defn eval-specs [& regions]
  (let [selected-region? (if (seq regions)
                           (->> regions
                                (map name)
                                (into #{}))
                           (constantly true))]
    (binding [*ns* *ns*]
      (doseq [f (->> region-specs
                     (filter (comp selected-region? :region))
                     (mapcat (partial parse-resources "crucible.generated")))]
        (try
          (eval f)
          (catch Exception e
            (throw (ex-info "Error generating specs" {:cause e :form f}))))))))

(defn generate-specs []
  (doseq [{:keys [region] :as r} region-specs]
    (let [f (str "generated-src/crucible/generated/" (.replaceAll region "-" "_") ".clj")]
      (io/make-parents f)
      (with-open [w (io/writer f)]
        (doseq [sexp (parse-resources "crucible.generated" r)]
          (.write w (pr-str sexp))
          (.write w "\n"))))))
