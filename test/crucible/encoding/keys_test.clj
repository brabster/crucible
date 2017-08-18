(ns crucible.encoding.keys-test
  (:require [crucible.encoding.keys :as keys]
            [crucible.core :as cru]
            [crucible.resources :as r]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))

(def testing-123-translation "Testing123Foo")

(defmethod keys/->key :testing-123 [_] testing-123-translation)

(s/def ::foo (s/keys :req [::testing-123]))

(def test-resource (r/resource-factory "Test::Test" ::foo))

(deftest ->key-translates-on-encode-template
  (testing "->key in element key position translates"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Parameters" {testing-123-translation {"Type" "String"}}}
           (cheshire.core/decode
            (cru/encode
             (cru/template "t"
                           :testing-123 (cru/parameter)))))))

  (testing "->key in element properties position translates"
    (is (get-in (cheshire.core/decode
                 (cru/encode
                  (cru/template "t"
                                :foo (test-resource {::testing-123 "foo"}))))
                ["Resources" "Foo" "Properties" "Testing123Foo"]))))
