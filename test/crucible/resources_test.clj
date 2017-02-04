(ns crucible.resources-test
  (:require [crucible.resources :as res]
            [crucible.values :as v]
            [clojure.test :refer :all]
            [clojure.spec :as s]))

(deftest resource-property-type
  (testing "single ref is valid"
    (is (s/valid? ::res/resource-property-value (v/xref :foo))))

  (testing "function is valid"
    (is (s/valid? ::res/resource-property-value (v/join "-" ["foo"])))
    (is (s/valid? ::res/resource-property-value (v/find-in-map :foo-map "bar" "baz")))))

(deftest resource-factory
  (testing "exception thrown if type does not look like a valid AWS resource type"
    (is (thrown? Exception (res/resource-factory "bob" ::foo))))

  (s/def ::foo (s/keys))

  (testing "factory function places type in ::type key"
    (let [type "AWS::Bob"]
      (is (= type
             (-> type
                 (res/resource-factory ::foo)
                 (apply [{}])
                 second
                 ::res/type))))))

(deftest resource-factory-validation
  (let [type "Custom::MyResource"
        spec (s/keys :req [::foo])
        my-resource (res/resource-factory type spec)]

    (testing "resource factory throws on invalid props"
      (is (thrown? Exception (my-resource {}))))

    (testing "resource factory constructs element on valid props"
      (is (= [:resource {::res/type type ::res/properties {::foo {}}}]
             (my-resource {::foo {}}))))))

(s/def ::meta-test-resource-spec any?)
(res/defresource meta-test-resource "AWS::Meta::Test" ::meta-test-resource-spec)

(deftest documentation-meta-test
  (testing "documentation is added"
    (is (-> #'meta-test-resource meta :doc)))
  (testing "documentation mentions AWS type"
    (is (.contains (-> #'meta-test-resource meta :doc) "AWS::Meta::Test"))))
