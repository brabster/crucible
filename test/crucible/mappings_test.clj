(ns crucible.mappings-test
  (:require [crucible.core :refer [mapping]]
            [crucible.encoding :as enc]
            [clojure.test :refer :all]))

(deftest mapping-rewrite-test
  (testing "Rewrite is a no-op"
    (is (= {"foo" {"bar" "baz"
                   "quxx" "fizz"}}
           (enc/rewrite-element-data (mapping "foo" {"bar" "baz"
                                                     "quxx" "fizz"}))))))
