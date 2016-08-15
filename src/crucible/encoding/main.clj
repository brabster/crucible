(ns crucible.encoding.main
  "Supports generating and writing templates to files. Intended for
  ad-hoc manual use and build tooling."
  (:gen-class)
  (:require [crucible.encoding :refer [encode]]
            [clojure.java.io :as io]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]))

(defn- write-templates [output-path [var-sym template]]
  (let [output-file (str output-path "/" var-sym ".json")]
    (io/make-parents output-file)
    (spit output-file template)
    output-file))

(defn write-template-vars [vars ns output-path]
  (->> vars
       (filter (fn [[k v]] (-> v deref meta :crucible.core/template)))
       (map (fn [[k v]] [k (-> v deref crucible.encoding/encode)]))
       (map (partial write-templates (str output-path "/" ns)))
       (reduce (fn [_ f] (println "Created template:" f)) [])))

(defn -main
  "Write the templates defined in the namespaces to the output path."
  [& args]
  (let [{:keys [template-namespaces output-path]}
        (s/conform (s/cat :template-namespaces (s/+ (s/or :sym symbol?
                                                          :str string?))
                          :output-path string?) args)
        template-namespace-symbols (map #(if (= (first %) :str)
                                           (symbol (second %))
                                           (second %)) template-namespaces)]
    (doseq [ns template-namespace-symbols]
      (require ns :reload-all)
      (-> ns
          ns-publics
          seq
          (write-template-vars ns output-path)))))
