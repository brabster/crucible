(ns crucible.encoding.values-test
  (:require [clojure.test :refer :all]
            [crucible.values :refer :all]))

(deftest test-values

  (testing "string value"
    (is (= "foo" (encode-value "foo"))))

  (testing "numeric value"
    (is (= 4 (encode-value 4))))

  (testing "ref value"
    (is (= {"Ref" "Foo"} (encode-value (xref :foo)))))

  (testing "select fn get-att value"
    (is (= {"Fn::GetAtt" ["Resource" "Foo"]} (encode-value (xref :resource :foo)))))

  (testing "pseudo-account value"
    (is (= {"Ref" "AWS::AccountId"} (encode-value (pseudo :account-id)))))

  (testing "pseudo-region value"
    (is (= {"Ref" "AWS::Region"} (encode-value (pseudo :region)))))

  (testing "pseudo-notification-arns value"
    (is (= {"Ref" "AWS::NotificationARNs"} (encode-value (pseudo :notification-arns)))))

  (testing "no-value value"
    (is (= {"Ref" "AWS::NoValue"} (encode-value (pseudo :no-value)))))

  (testing "stack-id value"
    (is (= {"Ref" "AWS::StackId"} (encode-value (pseudo :stack-id)))))

  (testing "stack-name value"
    (is (= {"Ref" "AWS::StackName"} (encode-value (pseudo :stack-name)))))

  (testing "join fn strings value"
    (is (= {"Fn::Join" ["." ["foo" "bar"]]} (encode-value (join "." ["foo" "bar"])))))

  (testing "select fn string value"
    (is (= {"Fn::Select" ["1" ["foo" "bar"]]} (encode-value (select 1 ["foo" "bar"])))))

  (testing "select fn select value"
    (is (= {"Fn::Select" ["1" ["foo" {"Ref" "Blah"}]]} (encode-value (select 1 ["foo" (xref :blah)])))))

  (testing "property value walk"
    (is (= {"Fn::Join" ["-" [{"Ref" "Foo"} "bar" {"Ref" "AWS::AccountId"}]]}
           (encode-value (join "-" [(xref :foo) "bar" (pseudo :account-id)]))))))

#_(deftest referenceable-test

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
