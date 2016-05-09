(ns crucible.values-test
  (:require [clojure.test :refer :all]
            [crucible.values :refer :all]))

(deftest test-values

  (testing "unknown key"
    (is (= {"Foo" "bar"} (convert-value nil [:foo "bar"]))))
  
  (testing "string value"
    (is (= "foo" (convert-value nil "foo"))))

  (testing "ref value"
    (is (= {"Ref" "Foo"} (convert-value {:parameters {:foo nil}} [:ref :foo]))))

  (testing "select fn get-att value"
    (is (= {"Fn::GetAtt" ["Resource" "Foo"]}
           (convert-value {:resources {:resource nil}} [:ref :resource :foo]))))

  (testing "pseudo-account value"
    (is (= {"Ref" "AWS::AccountId"} (convert-value nil [:pseudo :account-id]))))

  (testing "pseudo-region value"
    (is (= {"Ref" "AWS::Region"} (convert-value nil [:pseudo :region]))))

  (testing "pseudo-notification-arns value"
    (is (= {"Ref" "AWS::NotificationARNs"} (convert-value nil [:pseudo :notification-arns]))))

  (testing "no-value value"
    (is (= {"Ref" "AWS::NoValue"} (convert-value nil [:pseudo :no-value]))))

  (testing "stack-id value"
    (is (= {"Ref" "AWS::StackId"} (convert-value nil [:pseudo :stack-id]))))

  (testing "stack-name value"
    (is (= {"Ref" "AWS::StackName"} (convert-value nil [:pseudo :stack-name]))))

  (testing "join fn strings value"
    (is (= {"Fn::Join" ["." ["foo" "bar"]]}
           (convert-value nil [:fn [:join {:delimiter "." :values ["foo" "bar"]}]]))))

  (testing "select fn string value"
    (is (= {"Fn::Select" ["1" ["foo" "bar"]]}
           (convert-value nil [:fn [:select {:index "1" :values ["foo" "bar"]}]]))))

  (testing "select fn select value"
    (is (= {"Fn::Select" ["1" ["foo" {"Ref" "Blah"}]]}
           (convert-value {:parameters {:blah nil}}
                          [:fn [:select {:index "1" :values ["foo" [:ref :blah]]}]]))))

  (testing "property value walk"
    (is (= {"Fn::Join" ["-" [{"Ref" "Foo"} "bar" {"Ref" "AWS::AccountId"}]]}
           (convert-value {:parameters {:foo nil}}
                          [:fn [:join {:delimiter "-"
                                          :values [[:ref :foo]
                                                   "bar"
                                                   [:pseudo :account-id]]}]])))))

(deftest value-refs-test
  (testing "cannot reference an output"
    (is (thrown? AssertionError (convert-value {:outputs {:foo nil}}
                                               [:ref :foo]))))

  (testing "cannot reference non-existent element"
    (is (thrown? AssertionError (convert-value {:outputs {:foo nil}}
                                               [:ref :bar])))))

(deftest referenceable-test

  (testing "AssertionError if template is nil"
    (is (thrown? AssertionError (referenceable? nil :foo))))

  (testing "false if template is empty"
    (is (false? (referenceable? {:parameters {}} :foo))))
  
  (testing "true if ref is a parameter"
    (is (true? (referenceable? {:parameters {:foo nil}} :foo))))

  (testing "true if ref is a resource"
    (is (true? (referenceable? {:resources {:foo nil}} :foo))))

  (testing "false if ref is an output"
    (is (false? (referenceable? {:outputs {:foo nil}} :foo))))

  (testing "false if ref not present"
    (is (false? (referenceable? {:parameters {:bar nil}} :foo)))))
