(ns crucible.aws.ec2
  (:require [clojure.spec :as s]
            [crucible.resources :as r]
            [crucible.values :as v]))

(defn ec2 [suffix] (str "AWS::EC2::" suffix))

(s/def ::cidr-block (v/spec-or-ref string?))

(s/def ::vpc (s/keys :req [::cidr-block]
                     :opt [::enable-dns-support
                           ::enable-dns-hostnames
                           ::instance-tenancy
                           ::r/tags]))

(def vpc (r/resource-factory (ec2 "VPC") ::vpc))

(s/def ::vpc-id (v/spec-or-ref string?))
(s/def ::availability-zone (v/spec-or-ref string?))
(s/def ::map-public-ip-on-launch (v/spec-or-ref string?))

(s/def ::subnet (s/keys :req [::vpc-id ::cidr-block]
                        :opt [::availability-zone
                              ::map-public-ip-on-launch
                              ::r/tags]))

(def subnet (r/resource-factory (ec2 "Subnet") ::subnet))

(s/def ::domain #{"vpc"})
(s/def ::instance-id string?)

(s/def ::eip (s/keys :opt [::domain ::instance-id]))

(def eip (r/resource-factory (ec2 "EIP") ::eip))

(s/def ::allocation-id string?)
(s/def ::eip string?)
(s/def ::private-ip-address string?)

(s/def ::eip-association (s/keys :opt [::allocation-id
                                       ::eip
                                       ::instance-id
                                       ::network-interface-id
                                       ::private-ip-address]))

(def eip-association (r/resource-factory (ec2 "EIPAssociation") ::eip-association))

(s/def ::internet-gateway (s/? (s/keys :opt [::r/tags])))

(def internet-gateway (r/resource-factory (ec2 "InternetGateway") ::internet-gateway))

(s/def ::vpc-gateway-attachment (s/keys :req [::vpc-id]
                                        :opt [::internet-gateway-id
                                              ::vpn-gateway-id]))
