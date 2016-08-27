(ns crucible.assertion
  (:require [clojure.test :as test]
            [crucible.encoding :as enc]))

(def resource= 'encoded-as)

;; clojure.test doesn't print ex-data which is a pain with clojure.spec
;; remove when ex-data is printed on test failures by default...
(defmethod test/assert-expr 'encoded-as [msg form]
  (let [expected (nth form 1)
        resource (nth form 2)]
    `(let [result# (try (enc/rewrite-element-data ~resource)
                        (catch Exception e# (ex-data e#)))]
       (if (= ~expected result#)
         (test/do-report {:type :pass :expected ~expected :actual ~resource :message ~msg})
         (test/do-report {:type :fail :expected ~expected :actual result# :message ~msg})))))
