(ns crucible.spec.template_test
  (:require [crucible.spec.template :refer [template parameter resource output]]
            [crucible.spec.resources :as res]
            [crucible.spec.aws.ec2 :as ec2]))

(template "My funky template"
          :my-vpc-cidr  (parameter)
          :my-public-subnet-cidr (parameter)
          :my-vpc (resource (ec2/vpc {::ec2/cidr-block :my-vpc-cidr}))
          :my-public-subnet (resource (ec2/subnet {::ec2/vpc-id :my-vpc
                                                   ::ec2/cidr-block :my-public-subnet-cidr}))
          :my-igw (resource (ec2/internet-gateway))
          :my-eip (resource (ec2/eip {::ec2/domain "vpc"}
                                     {::res/deletion-policy ::res/retain}))
          :my-eip-assoc (resource (ec2/eip-association {::ec2/allocation-id "foo"}))
          :vpc-id (output :description "the vpc id" :value :my-vpc-id))
