(ns crucible.custom-resource-test
  (:require  [clojure.test :refer :all]
             [crucible.assertion :refer [resource=]]
             [crucible.aws.custom-resource :as custom]
             [cheshire.core :as json]))

(deftest custom-resource-apitest
  (let [service-token "arn:foo"
        expected {"Type" "Custom::MyResource"
                  "Properties" {"ServiceToken" service-token}}]

    (testing "higher-order resource fn"
      (is (resource= expected
                     ((custom/resource "MyResource")
                      {::custom/service-token service-token}))))

    (testing "higher-order resource fn"
      (is (resource= expected
                     (custom/resource "MyResource"
                                      {::custom/service-token service-token}))))))
