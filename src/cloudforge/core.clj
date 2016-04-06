(ns cloudforge.core)

(declare convert-value)

(defn convert-fn-join
  [spec]
  {"Fn::Join" [(:delimiter spec) (into [] (map #(convert-value %) (:values spec)))]})

(defn convert-fn-select
  [spec]
  {"Fn::Select" [(:index spec) (into [] (map #(convert-value %) (:values spec)))]})

(defn convert-fn
  [f]
  (let [type (first f)
        spec (second f)]
    (cond (= type :join) (convert-fn-join spec)
          (= type :select) (convert-fn-select spec))))

(defn convert-pseudo
  [type]
  (cond (= type :account-id) {"Ref" "AWS::AccountId"}
        (= type :region) {"Ref" "AWS::Region"}
        (= type :notification-arns) {"Ref" "AWS::NotificationARNs"}
        (= type :no-value) {"Ref" "AWS::NoValue"}
        (= type :stack-id) {"Ref" "AWS::StackId"}
        (= type :stack-name) {"Ref" "AWS::StackName"}))

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

