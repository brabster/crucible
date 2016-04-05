(ns cloudforge.core)

(declare convert-value)

(defn convert-fn-join
  [spec]
  {"Fn::Join" [(:delimiter spec) (into [] (map #(convert-value %) (:values spec)))]})

(defn convert-fn
  [f]
  (let [type (first f)
        spec (second f)]
    (cond (= type :join) (convert-fn-join spec))))

(defn convert-pseudo
  [type]
  (cond (= type :account) {"Ref" "AWS::Account"}))

(defn convert-ref
  [r]
  {"Ref" (name r)})

(defn convert-value
  [v]
  (if (string? v) v
      (let [type (first v)
            spec (second v)]
        (cond (= type :fn) (convert-fn spec)
              (= type :pseudo) (convert-pseudo spec)
              (= type :ref) (convert-ref spec)))))

(defn encode
  [map]
  (convert-value map))

