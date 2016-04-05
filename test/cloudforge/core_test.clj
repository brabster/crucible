(ns cloudforge.core-test
  (:require [clojure.test :refer :all]
            [cloudforge.core :refer :all]))

(deftest value-conversions

  (testing "string value"
    (is (= "foo" (encode "foo"))))

  (testing "ref value"
    (is (= {"Ref" "x"} (encode [:ref "x"]))))

  (testing "pseudo-account value"
    (is (= {"Ref" "AWS::Account"} (encode [:pseudo :account]))))

  (testing "join fn strings value"
    (is (= {"Fn::Join" ["." ["foo" "bar"]]}
           (encode [:fn [:join {:delimiter "." :values ["foo" "bar"]}]]))))
  
  (testing "property value walk"
    (is (= {"Fn::Join" ["-" [{"Ref" "foo"} "bar" {"Ref" "AWS::Account"}]]}
           (encode [:fn [:join {:delimiter "-"
                                :values [[:ref :foo]
                                         "bar"
                                         [:pseudo :account]]}]])))))
