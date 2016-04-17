(ns crucible.core-test
  (:require [clojure.test :refer :all]
            [crucible.core :refer :all]))

(deftest value-conversions

  (testing "unknown key"
    (is (= {"Foo" "bar"} (encode [:foo "bar"]))))
  
  (testing "string value"
    (is (= "foo" (encode "foo"))))

  (testing "ref value"
    (is (= {"Ref" "x"} (encode [:ref "x"]))))

  (testing "select fn get-att value"
    (is (= {"Fn::GetAtt" ["resource" "foo"]}
           (encode [:ref :resource :foo]))))

  (testing "pseudo-account value"
    (is (= {"Ref" "AWS::AccountId"} (encode [:pseudo :account-id]))))

  (testing "pseudo-region value"
    (is (= {"Ref" "AWS::Region"} (encode [:pseudo :region]))))

  (testing "pseudo-notification-arns value"
    (is (= {"Ref" "AWS::NotificationARNs"} (encode [:pseudo :notification-arns]))))

  (testing "no-value value"
    (is (= {"Ref" "AWS::NoValue"} (encode [:pseudo :no-value]))))

  (testing "stack-id value"
    (is (= {"Ref" "AWS::StackId"} (encode [:pseudo :stack-id]))))

  (testing "stack-name value"
    (is (= {"Ref" "AWS::StackName"} (encode [:pseudo :stack-name]))))

  (testing "join fn strings value"
    (is (= {"Fn::Join" ["." ["foo" "bar"]]}
           (encode [:fn [:join {:delimiter "." :values ["foo" "bar"]}]]))))

  (testing "select fn string value"
    (is (= {"Fn::Select" ["1" ["foo" "bar"]]}
           (encode [:fn [:select {:index "1" :values ["foo" "bar"]}]]))))

  (testing "select fn select value"
    (is (= {"Fn::Select" ["1" ["foo" {"Ref" "blah"}]]}
           (encode [:fn [:select {:index "1" :values ["foo" [:ref :blah]]}]]))))

  (testing "property value walk"
    (is (= {"Fn::Join" ["-" [{"Ref" "foo"} "bar" {"Ref" "AWS::AccountId"}]]}
           (encode [:fn [:join {:delimiter "-"
                                :values [[:ref :foo]
                                         "bar"
                                         [:pseudo :account-id]]}]])))))
