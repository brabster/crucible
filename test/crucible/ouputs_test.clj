(ns crucible.outputs-test
  (:require [clojure.test :refer :all]
            [crucible.outputs :refer :all]))

(deftest output-conversions

  (testing "minimal output"
    (is (= {"Value" {"Ref" "foo"}}
           (encode-output :value [:ref :foo]))))
  
  (testing "output with description"
    (is (= {"Description" "foo"
            "Value" {"Ref" "bar"}}
           (encode-output :description "foo"
                          :value [:ref :bar])))))


