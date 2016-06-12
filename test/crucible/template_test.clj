(ns crucible.template-test
  (:require [clojure.test :refer :all]
            [crucible.template :refer [template parameter resource output xref encode]]
            [crucible.values :as v]
            [crucible.pseudo :as ps]
            [crucible.resources :as res]
            [crucible.aws.ec2 :as ec2]))

(deftest minimal-template
  (is (= {:description "t",
          :elements
          {:igw
           [:resource
            {::res/type "AWS::EC2::InternetGateway",
             ::res/properties nil}]}}
         (template "t" :igw (ec2/internet-gateway)))))

(deftest minimal-template
  (is (= {:description "t",
          :elements
          {:vpc
           [:resource
            {::res/type "AWS::EC2::VPC",
             ::res/properties
             {::ec2/cidr-block "1.2.3.4/24"}}],
           :subnet
           [:resource
            {::res/type "AWS::EC2::Subnet",
             ::res/properties
             {::ec2/cidr-block "1.2.3.4/16",
              ::ec2/vpc-id
              {::v/type ::v/xref,
               ::v/ref :foo}}}]}}
         (template "t"
                   :vpc (ec2/vpc {::ec2/cidr-block "1.2.3.4/24"})
                   :subnet (ec2/subnet {::ec2/cidr-block "1.2.3.4/16"
                                        ::ec2/vpc-id (xref :foo)})))))

(deftest template-with-param
  (is (= {:description "t",
          :elements
          {:vpc-cidr
           [:parameter
            {:crucible.parameters/type :crucible.parameters/string}],
           :vpc
           [:resource
            {:crucible.resources/type "AWS::EC2::VPC",
             :crucible.resources/properties
             {:crucible.aws.ec2/cidr-block
              {:crucible.values/type :crucible.values/xref,
               :crucible.values/ref :vpc-cidr}}}]}}
         (template "t"
                   :vpc-cidr (parameter)
                   :vpc (ec2/vpc {::ec2/cidr-block (xref :vpc-cidr)})))))

(deftest template-with-output
  (is (= {:description "t",
          :elements
          {:vpc-id
           [:output
            {:crucible.outputs/description "the vpc id",
             :crucible.outputs/value
             {:crucible.values/type :crucible.values/xref,
              :crucible.values/ref :vpc}}],
           :vpc
           [:resource
            {:crucible.resources/type "AWS::EC2::VPC",
             :crucible.resources/properties
             {:crucible.aws.ec2/cidr-block
              {:crucible.values/type :crucible.values/xref,
               :crucible.values/ref :vpc-cidr}}}]}}
         (template "t"
                   :vpc (ec2/vpc {::ec2/cidr-block (xref :vpc-cidr)})
                   :vpc-id (output (xref :vpc) "the vpc id")))))

(deftest join-fn-in-value-position
  (is (= {:description "t",
          :elements
          {:vpc-cidr
           [:parameter
            {:crucible.parameters/type :crucible.parameters/string}],
           :vpc
           [:resource
            {:crucible.resources/type "AWS::EC2::VPC",
             :crucible.resources/properties
             {:crucible.aws.ec2/cidr-block
              {:crucible.values/type :crucible.values/join,
               :crucible.values/values
               ["foo"
                {:crucible.values/type :crucible.values/xref,
                 :crucible.values/ref :vpc-cidr}],
               :crucible.values/delimiter "-"}}}]}}
         (template "t"
                   :vpc-cidr (parameter)
                   :vpc (ec2/vpc {::ec2/cidr-block (v/join "-" ["foo" (xref :vpc-cidr)])})))))
