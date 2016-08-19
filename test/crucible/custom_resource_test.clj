(ns crucible.custom-resource-test
  (:require  [clojure.test :refer :all]
             [crucible.core :refer [encode template]]
             [crucible.aws.custom-resource :as custom]
             [cheshire.core :as json]))

(deftest custom-resource-apitest
  (let [service-token "arn:foo"
        expected {"AWSTemplateFormatVersion" "2010-09-09"
                  "Description" "minimal"
                  "Resources"
                  {"MyCustomResource"
                   {"Type" "Custom::MyResource"
                    "Properties" {"ServiceToken" service-token}}}}]

    (testing "higher-order resource fn"
      (is (= expected
             (json/decode
              (encode
               (template
                "minimal"
                :my-custom-resource
                ((custom/resource "MyResource")
                 #::custom{:service-token service-token})))))))

    (testing "higher-order resource fn"
      (is (= expected
             (json/decode
              (encode
               (template
                "minimal"
                :my-custom-resource
                (custom/resource "MyResource" #::custom{:service-token service-token})))))))))
