(ns crucible.template-test
  (:require [clojure.test :refer :all]
            [crucible.core :refer [template parameter resource output xref encode]]
            [crucible.parameters :as param]
            [crucible.outputs :as out]
            [crucible.values :as v]
            [crucible.resources :as res]
            [crucible.aws.ec2 :as ec2]))

(deftest template-metadata-tag
  (is (true? (-> "t"
                 template
                 meta
                 :crucible.core/template))))

(deftest minimal-template
  (is (= {:description "t"
          :elements
          {:igw {:type :resource
                 :specification {::res/type "AWS::EC2::InternetGateway"
                                 ::res/properties nil}}}}
         (template "t" :igw (ec2/internet-gateway)))))

(deftest two-element-template
  (is (= {:description "t"
          :elements
          {:vpc {:type :resource
                 :specification {::res/type "AWS::EC2::VPC"
                                 ::res/properties
                                 {::ec2/cidr-block "1.2.3.4/24"}}}
           :subnet {:type :resource
                    :specification {::res/type "AWS::EC2::Subnet"
                                    ::res/properties {::ec2/cidr-block "1.2.3.4/16"
                                                      ::ec2/vpc-id {::v/type ::v/xref,
                                                                    ::v/ref :subnet}}}}}}
         (template "t"
                   :vpc (ec2/vpc {::ec2/cidr-block "1.2.3.4/24"})
                   :subnet (ec2/subnet {::ec2/cidr-block "1.2.3.4/16"
                                        ::ec2/vpc-id (xref :subnet)})))))

(deftest template-with-param
  (is (= {:description "t"
          :elements {:vpc-cidr {:type :parameter
                                :specification {::param/type ::param/string}}
                     :vpc {:type :resource
                           :specification {::res/type "AWS::EC2::VPC"
                                           ::res/properties {::ec2/cidr-block
                                                             {::v/type ::v/xref
                                                              ::v/ref :vpc-cidr}}}}}}
         (template "t"
                   :vpc-cidr (parameter)
                   :vpc (ec2/vpc {::ec2/cidr-block (xref :vpc-cidr)})))))

(deftest template-with-output
  (is (= {:description "t"
          :elements
          {:vpc-cidr {:type :parameter
                      :specification {::param/type ::param/string}}
           :vpc-id
           {:type :output
            :specification {::out/description "the vpc id"
                            ::out/value {::v/type ::v/xref
                                         ::v/ref :vpc}}}
           :vpc
           {:type :resource
            :specification {::res/type "AWS::EC2::VPC"
                            ::res/properties {::ec2/cidr-block {::v/type ::v/xref
                                                                ::v/ref :vpc-cidr}}}}}}
         (template "t"
                   :vpc-cidr (parameter)
                   :vpc (ec2/vpc {::ec2/cidr-block (xref :vpc-cidr)})
                   :vpc-id (output (xref :vpc) "the vpc id")))))

(deftest template-with-exported-output
  (is (= {:description "t"
          :elements
          {:vpc-cidr {:type :parameter
                      :specification {::param/type ::param/string}}
           :vpc-id
           {:type :output
            :specification {::out/description "the vpc id"
                            ::out/value {::v/type ::v/xref
                                         ::v/ref :vpc}
                            ::out/export {::out/export-name "foo"}}}
           :vpc
           {:type :resource
            :specification {::res/type "AWS::EC2::VPC"
                            ::res/properties {::ec2/cidr-block {::v/type ::v/xref
                                                                ::v/ref :vpc-cidr}}}}}}
         (template "t"
                   :vpc-cidr (parameter)
                   :vpc (ec2/vpc {::ec2/cidr-block (xref :vpc-cidr)})
                   :vpc-id (output (xref :vpc) "the vpc id" "foo")))))

(deftest join-fn-in-value-position
  (is (= {:description "t"
          :elements
          {:vpc-cidr
           {:type :parameter
            :specification {::param/type ::param/string}}
           :vpc
           {:type :resource
            :specification {::res/type "AWS::EC2::VPC"
                            ::res/properties
                            {::ec2/cidr-block
                             {::v/type ::v/join
                              ::v/fn-values ["foo" {::v/type ::v/xref
                                                    ::v/ref :vpc-cidr}]
                              ::v/delimiter "-"}}}}}}
         (template "t"
                   :vpc-cidr (parameter)
                   :vpc (ec2/vpc {::ec2/cidr-block (v/join "-" ["foo" (xref :vpc-cidr)])})))))
