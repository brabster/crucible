(ns crucible.encoding.main
  "Supports generating and writing templates to files. Intended for
  ad-hoc manual use and build tooling."
  (:gen-class)
  (:require [crucible.encoding :refer [encode]]
            [clojure.java.io :as io]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]))

(s/fdef -main
        :args (s/cat :template-require string?
                     :output-path string?))

(defn -main
  "Write the template defined by the last form in template-require to the output-path. Parameter output-path is a string describing the path to the file we will generate. Parameter template-require is a string describing the path to the target template."
  [& [template-path output-path]]
  (let [template (load-file template-path)]
    (io/make-parents output-path)
    (spit output-path (encode @template))))

(stest/instrument `-main)
