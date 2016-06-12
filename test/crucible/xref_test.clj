(ns crucible.xref-test
  (:require  [clojure.test :refer :all]
             [crucible.values :as v]
             [clojure.spec :as s]))

(deftest valid-xref
  (testing "plain xref validates"
    (is (s/valid? ::v/xref (v/xref :foo))))

  (testing "xref with attribute"
    (is (s/valid? ::v/xref (v/xref :foo :bar)))))
