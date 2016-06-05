(ns crucible.spec.resources.aws.ec2
  (:require [clojure.spec :as s]
            [crucible.spec.resources :refer [resource-factory]]))

(s/def ::cidr-block string?)

(def vpc (resource-factory "AWS::EC2::VPC"))
(defmethod p/properties-type ::vpc [_]
  (s/keys :req [::cidr-block]
          :opt [::enable-dns-support
                ::enable-dns-hostnames
                ::instance-tenancy
                ::tags]))

(def subnet (resource-factory "AWS::EC2::Subnet"))
(defmethod p/properties-type ::subnet [_]
  (s/keys :req [::vpc-id ::cidr-block]
          :opt [::availability-zone
                ::map-public-ip-on-launch
                ::tags]))

