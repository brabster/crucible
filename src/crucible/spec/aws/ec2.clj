(ns crucible.spec.aws.ec2
  (:require [clojure.spec :as s]
            [crucible.spec.resources :as r]
            [crucible.spec.values :as v]))

(defn ec2 [suffix] (str "AWS::EC2::" suffix))

(s/def ::cidr-block ::v/value)

(defmethod r/properties-type ::vpc [_]
  (s/keys :req [::cidr-block]
          :opt [::enable-dns-support
                ::enable-dns-hostnames
                ::instance-tenancy
                ::r/tags]))

(def vpc (r/resource-factory (ec2 "VPC") ::vpc))

(s/def ::vpc-id ::v/value)
(s/def ::availability-zone ::v/value)
(s/def ::map-public-ip-on-launch ::v/value)

(defmethod r/properties-type ::subnet [_]
  (s/keys :req [::vpc-id ::cidr-block]
          :opt [::availability-zone
                ::map-public-ip-on-launch
                ::r/tags]))

(def subnet (r/resource-factory (ec2 "Subnet") ::subnet))

(s/def ::domain #{"vpc"})
(s/def ::instance-id string?)

(defmethod r/properties-type ::eip [_]
  (s/keys :opt [::domain ::instance-id]))

(def eip (r/resource-factory (ec2 "EIP") ::eip))

(s/def ::allocation-id string?)
(s/def ::eip string?)
(s/def ::private-ip-address string?)

(defmethod r/properties-type ::eip-association [_]
  (s/keys :opt [::allocation-id ::eip ::instance-id ::network-interface-id ::private-ip-address]))

(def eip-association (r/resource-factory (ec2 "EIPAssociation") ::eip-association))

(defmethod r/properties-type ::internet-gateway [_]
  (s/keys :opt [::r/tags]))

(def internet-gateway (r/resource-factory (ec2 "InternetGateway") ::internet-gateway))

(s/def ::vpc-id ::v/value)

(defmethod r/properties-type ::vpc-gateway-attachment [_]
  (s/keys :req [::vpc-id]
          :opt [::internet-gateway-id
                ::vpn-gateway-id]))
