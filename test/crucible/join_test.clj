(ns crucible.join-test
  (:require  [clojure.test :refer :all]
             [clojure.spec :as s]
             [crucible.values :as v]))

(deftest join-test
  (testing "join no elements is not valid"
    (is (not (s/valid? ::v/join (v/join "-" [])))))

  (testing "join one string arg is valid"
    (is (s/valid? ::v/join (v/join "-" ["foo"]))))

  (testing "join one xref arg is valid"
    (is (s/valid? ::v/join (v/join "-" [(v/xref :foo)]))))

  (testing "join multiple xref args is valid"
    (is (s/valid? ::v/join (v/join "-" [(v/xref :foo) (v/xref :bar :baz)]))))

  (testing "join multiple xref and string args is valid"
    (is (s/valid? ::v/join (v/join "-" ["foo" (v/xref :bar)])))))
