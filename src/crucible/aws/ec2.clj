(ns crucible.aws.ec2
  "Resources in AWS::EC2::*"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(defn ec2 [suffix] (str "AWS::EC2::" suffix))

(s/def ::cidr-block (spec-or-ref string?))

(s/def ::vpc (s/keys :req [::cidr-block]
                     :opt [::enable-dns-support
                           ::enable-dns-hostnames
                           ::instance-tenancy
                           ::res/tags]))

(defresource vpc (ec2 "VPC") ::vpc)

(s/def ::vpc-id (spec-or-ref string?))
(s/def ::availability-zone (spec-or-ref string?))
(s/def ::map-public-ip-on-launch (spec-or-ref string?))

(s/def ::subnet (s/keys :req [::vpc-id ::cidr-block]
                        :opt [::availability-zone
                              ::map-public-ip-on-launch
                              ::tags]))

(defresource subnet (ec2 "Subnet") ::subnet)

(s/def ::domain #{"vpc"})
(s/def ::instance-id string?)

(s/def ::eip (s/keys :opt [::domain ::instance-id]))

(defresource eip (ec2 "EIP") ::eip)

(s/def ::allocation-id (spec-or-ref string?))
(s/def ::subnet-id (spec-or-ref string?))
(s/def ::eip string?)
(s/def ::private-ip-address string?)

(s/def ::eip-association (s/keys :opt [::allocation-id
                                       ::eip
                                       ::instance-id
                                       ::network-interface-id
                                       ::private-ip-address]))

(defresource eip-association (ec2 "EIPAssociation") ::eip-association)

(defresource internet-gateway (ec2 "InternetGateway") (s/? (s/keys :opt [::tags])))

(defresource nat-gateway (ec2 "NatGateway") (s/keys :req [::allocation-id ::subnet-id]
                                                         :opt [::tags]))

(s/def ::vpc-gateway-attachment (s/keys :req [::vpc-id]
                                        :opt [::internet-gateway-id
                                              ::vpn-gateway-id]))

(s/def ::group-description (spec-or-ref string?))

(s/def ::cidr-ip (spec-or-ref string?))
(def highest-port 65535)
(def lowest-port 1)
(s/def ::port (spec-or-ref (s/and integer?
                                  #(<= lowest-port % highest-port))))

(s/def ::from-port ::port)
(s/def ::to-port ::port)

(def protocols-all -1)
(s/def ::ip-protocol (spec-or-ref (s/or :int (s/and integer?
                                                    #(<= -1 %))
                                        :str #{"tcp" "udp" "icmp"})))

(s/def ::security-group-id (spec-or-ref string?))

(s/def ::source-security-group-id ::security-group-id)
(s/def ::source-security-group-name (spec-or-ref string?))
(s/def ::source-security-group-owner-id (spec-or-ref string?))

(s/def ::security-group-ingress (s/* (s/keys :req [::ip-protocol]
                                             :opt [::cidr-ip
                                                   ::from-port
                                                   ::to-port
                                                   ::source-security-group-id
                                                   ::source-security-group-name
                                                   ::source-security-group-owner-id])))

(s/def ::destination-security-group-id ::security-group-id)

(s/def ::security-group-egress (s/* (s/keys :req [::ip-protocol]
                                            :opt [::from-port
                                                  ::to-port
                                                  ::destination-security-group-id])))

(s/def ::security-group (s/keys :req [::group-description]
                                :opt [::security-group-ingress
                                      ::security-group-egress
                                      ::res/tags
                                      ::vpc-id]))

(defresource security-group (ec2 "SecurityGroup") ::security-group)
