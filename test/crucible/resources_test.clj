(ns crucible.resources-test
  (:require [crucible.resources :as r]
            [clojure.test :refer :all]
            [clojure.spec :as s]))

(deftest resource-property-type
  (testing "single value is valid"
    (is (s/valid? ::r/resource-property-value "foo")))

  (testing "multiple values vector is valid"
    (is (s/valid? ::r/resource-property-value ["foo" "foo"])))

  (testing "vector of maps is valid"
    (is (s/valid? ::r/resource-property-value [{} {} {}]))))

(deftest resource-factory
  (testing "exception thrown if type does not look like a valid AWS resource type"
    (is (thrown? Exception (r/resource-factory "bob" ::foo))))

  (testing "factory function places type in ::type key"
    (let [type "AWS::Bob"]
      (is (-> type
              (r/resource-factory ::foo)
              (apply {})
              ::r/type
              (= type))))))
(deftest resource-factory-validation
  (let [type "Custom::MyResource"
        spec (s/keys :req [::foo])
        my-resource (r/resource-factory type spec)]

    (testing "resource factory throws on invalid props"
      (is (thrown? Exception (my-resource {}))))

    (testing "resource factory constructs element on valid props"
      (is (= {::r/type type
              ::r/properties {::foo ::bar}}
             (my-resource {::foo ::bar}))))))
