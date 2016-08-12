(ns crucible.values-test
  (:require [crucible
             [core :as cru]
             [values :as v]]
            [clojure.test :refer :all]
            [clojure.spec :as s]))

(deftest join-test
  (testing "no values" (is (s/valid? ::v/value (cru/join "-" []))))
  (testing "single literal" (is (s/valid? ::v/value (cru/join "-" ["foo"]))))
  (testing "single ref" (is (s/valid? ::v/value (cru/join "-" [(cru/xref :foo)]))))
  (testing "multiple mixed" (is (s/valid? ::v/value (cru/join "-" ["foo" (cru/xref :foo)]))))
  (testing "no delimiter" (is (s/valid? ::v/value (cru/join ["foo" (cru/xref :foo)])))))

(deftest select-test
  (testing "no values" (is (s/valid? ::v/value (cru/select 0 []))))
  (testing "single literal" (is (s/valid? ::v/value (cru/select 0 ["foo"]))))
  (testing "single ref" (is (s/valid? ::v/value (cru/select 0 [(cru/xref :foo)]))))
  (testing "multiple mixed" (is (s/valid? ::v/value (cru/select 0 ["foo" (cru/xref :foo)]))))

  (testing "1-index" (is (s/valid? ::v/value (cru/select 1 ["foo" (cru/xref :foo)])))))
