(ns cloudformation.infrastructure
  (:require [crucible.aws.s3 :as s3]
            [crucible.core :refer [template xref parameter output]]
            [crucible.policies :as policies]
            [myproject.hello]))

(def my-template
  (template "A simple demo template"
            :bucket-name (parameter :default (str (-> 'myproject.hello the-ns str) "-repo"))
            :bucket (s3/bucket {::s3/access-control "PublicRead"
                                ::s3/website-configuration {::s3/index-document "index.html"
                                                            ::s3/error-document "error.html"}}
                               (policies/deletion ::policies/retain))
            :website-domain (output (xref :bucket :domain-name))))
