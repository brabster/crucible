(ns cloudforge.values)

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
        spec (rest f)]
    (cond (= type :join) (apply convert-fn-join spec)
          (= type :select) (apply convert-fn-select spec))))

(defn convert-pseudo
  [type]
  (cond (= type :account-id) {"Ref" "AWS::AccountId"}
        (= type :region) {"Ref" "AWS::Region"}
        (= type :notification-arns) {"Ref" "AWS::NotificationARNs"}
        (= type :no-value) {"Ref" "AWS::NoValue"}
        (= type :stack-id) {"Ref" "AWS::StackId"}
        (= type :stack-name) {"Ref" "AWS::StackName"}))

(defn convert-ref
  ([r]
   {"Ref" (name r)})
  ([r att]
   {"Fn::GetAtt" [(name r) (name att)]}))

(defn convert-value
  [v]
  (if (string? v) v
      (let [type (first v)
            spec (rest v)]
        (cond (= type :fn) (apply convert-fn spec)
              (= type :pseudo) (apply convert-pseudo spec)
              (= type :ref) (apply convert-ref spec)))))

