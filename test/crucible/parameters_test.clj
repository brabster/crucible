(ns crucible.parameters-test
  (:require [clojure.test :refer :all]
            [crucible.parameters :refer :all]))

(def template nil)

(deftest parameter-conversions

  (testing "minimal string parameter"
    (is (= {"Type" "String"}
           (encode-parameter template {:type :string}))))
  
  (testing "minimal number parameter"
    (is (= {"Type" "Number"}
           (encode-parameter template {:type :number}))))

  (testing "full-specified string parameter"
    (is (= {"Type" "String"
            "Description" "desc"
            "Default" "foo"
            "NoEcho" "true"
            "AllowedValues" ["foo" "bar" "baz"]}
           (encode-parameter template {:type :string
                              :description "desc"
                              :default-value "foo"
                              :allowed-values ["foo" "bar" "baz"]
                              :no-echo true})))))


