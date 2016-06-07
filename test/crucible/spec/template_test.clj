(ns crucible.spec.template_test
  (:require [crucible.spec.template :as t]
            [crucible.spec.resources :as r]
            [crucible.spec.aws.ec2 :as ec2]
            [crucible.spec.parameters :as p]))

(t/template "My funky template"
            :my-vpc-cidr {::p/type ::p/string}
            :my-public-subnet-cidr {::p/type ::p/string}
            :my-vpc (ec2/vpc {::ec2/cidr-block :my-vpc-cidr})
            :my-public-subnet (ec2/subnet {::ec2/vpc-id :my-vpc
                                           ::ec2/cidr-block :my-public-subnet-cidr})
            :my-igw (ec2/internet-gateway)
            :my-eip (ec2/eip {::ec2/domain "vpc"}
                             {::r/deletion-policy ::r/retain})
            :my-eip-assoc (ec2/eip-association {::ec2/allocation-id "foo"}))
