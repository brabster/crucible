(ns crucible.values-test
  (:require [crucible.values :as v]
            [clojure.test :refer :all]
            [clojure.spec :as s]))

(deftest join-test
  (testing "no values" (is (s/valid? ::v/value (v/join "-" []))))
  (testing "single literal" (is (s/valid? ::v/value (v/join "-" ["foo"]))))
  (testing "single ref" (is (s/valid? ::v/value (v/join "-" [(v/xref :foo)]))))
  (testing "multiple mixed" (is (s/valid? ::v/value (v/join "-" ["foo" (v/xref :foo)]))))
  (testing "no delimiter" (is (s/valid? ::v/value (v/join ["foo" (v/xref :foo)])))))

(deftest select-test
  (testing "no values" (is (s/valid? ::v/value (v/select 0 []))))
  (testing "single literal" (is (s/valid? ::v/value (v/select 0 ["foo"]))))
  (testing "single ref" (is (s/valid? ::v/value (v/select 0 [(v/xref :foo)]))))
  (testing "multiple mixed" (is (s/valid? ::v/value (v/select 0 ["foo" (v/xref :foo)]))))

  (testing "1-index" (is (s/valid? ::v/value (v/select 1 ["foo" (v/xref :foo)])))))
