(ns crucible.values-test
  (:require [crucible
             [core :as cru]
             [values :as v]]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))

(deftest join-test
  (testing "no values" (is (s/valid? ::v/value (cru/join "-" []))))
  (testing "single literal" (is (s/valid? ::v/value (cru/join "-" ["foo"]))))
  (testing "single ref" (is (s/valid? ::v/value (cru/join "-" [(cru/xref :foo)]))))
  (testing "multiple mixed" (is (s/valid? ::v/value (cru/join "-" ["foo" (cru/xref :foo)]))))
  (testing "no delimiter" (is (s/valid? ::v/value (cru/join ["foo" (cru/xref :foo)])))))

(deftest if-test
  (testing "no values" (is (s/valid? ::v/value (cru/fn-if "-" "a" "b")))))

(deftest select-test
  (testing "no values" (is (s/valid? ::v/value (cru/select 0 []))))
  (testing "single literal" (is (s/valid? ::v/value (cru/select 0 ["foo"]))))
  (testing "single ref" (is (s/valid? ::v/value (cru/select 0 [(cru/xref :foo)]))))
  (testing "multiple mixed" (is (s/valid? ::v/value (cru/select 0 ["foo" (cru/xref :foo)]))))

  (testing "1-index" (is (s/valid? ::v/value (cru/select 1 ["foo" (cru/xref :foo)])))))

(deftest find-in-map-test
  (testing "all literals" (is (s/valid? ::v/value (cru/find-in-map :foo "bar" "baz"))))
  (testing "both refs" (is (s/valid? ::v/value (cru/find-in-map :foo
                                                                (cru/xref :bar)
                                                                (cru/xref :baz)))))
  (testing "mixed" (is (s/valid? ::v/value (cru/find-in-map :foo "bar" (cru/xref :baz))))))

(deftest import-test
  (testing "import name" (is (s/valid? ::v/value (cru/import-value "foo")))))

(deftest sub-test
  (testing "substitution string" (is (s/valid? ::v/value (cru/sub "${foo} bar")))))
