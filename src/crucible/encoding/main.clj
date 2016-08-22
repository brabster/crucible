(ns crucible.encoding.main
  "Supports generating and writing templates to files. Intended for
  ad-hoc manual use and build tooling."
  (:gen-class)
  (:require [crucible.encoding :refer [encode]]
            [clojure.string :as string]
            [clojure.tools.namespace.repl :as ns-repl]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]))

(defn template-var->write-location [tvar]
  (let [template-ns (ns-name (:ns (meta tvar)))
        template-name (:name (meta tvar))]
    (str (string/replace template-ns #"[.]" "/")
         "/"
         template-name
         ".json")))

(defn write-template [output-location template-var]
  (let [output-file (str output-location "/" (template-var->write-location template-var))
        template (encode @template-var)]
    (io/make-parents output-file)
    (spit output-file template)
    output-file))

(defn find-templates []
  (ns-repl/refresh)
  (mapcat #(->> %
                ns-publics
                seq
                (filter (fn [[k v]] (-> v deref meta :crucible.core/template)))
                (map (fn [[_ template-var]] template-var)))
          (all-ns)))

(def cli-options [["-o" "--output-directory DIRECTORY" "Output Directory"
                   :default "target/templates"]
                  ["-v" "--verbose"]
                  ["-h" "--help"]])

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  "Write the templates defined in the namespaces to the output path."
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 summary)
      errors (exit 1 errors))
    (doseq [template-var (find-templates)]
      (println "Created template:" (write-template (:output-directory options) template-var)))))
