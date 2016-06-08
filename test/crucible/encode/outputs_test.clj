(ns crucible.outputs-test
  (:require [clojure.test :refer :all]
            [crucible.outputs :refer :all]))

(deftest output-conversions

  (testing "minimal output"
    (is (= {"Value" {"Ref" "Foo"}}
           (encode-output {:parameters {:foo nil}} [:ref :foo]))))
  
  (testing "output with description"
    (is (= {"Description" "foo"
            "Value" {"Ref" "Bar"}}
           (encode-output {:parameters {:bar nil}} [:ref :bar] "foo")))))


