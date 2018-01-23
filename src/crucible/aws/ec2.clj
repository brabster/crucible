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
(s/def ::private-ip-address string?)

(s/def ::route-table (s/keys :req [::vpc-id]
                             :opt [::res/tags]))

(defresource route-table (ec2 "RouteTable") ::route-table)

(s/def ::route-table-id (spec-or-ref string?))
(s/def ::destination-cidr-block (spec-or-ref string?))
(s/def ::destination-ipv6-cidr-block (spec-or-ref string?))
(s/def ::egress-only-internet-gateway-id (spec-or-ref string?))
(s/def ::gateway-id (spec-or-ref string?))
(s/def ::instance-id (spec-or-ref string?))
(s/def ::nat-gateway-id (spec-or-ref string?))
(s/def ::network-interface-id (spec-or-ref string?))
(s/def ::vpc-peering-connection-id (spec-or-ref string?))

(s/def ::route (s/keys :req [::route-table-id]
                       :opt [::destination-cidr-block
                             ::destination-ipv6-cidr-block
                             ::egress-only-internet-gateway-id
                             ::gateway-id
                             ::instance-id
                             ::nat-gateway-id
                             ::network-interface-id
                             ::vpc-peering-connection-id]))

(defresource route (ec2 "Route") ::route)

(s/def ::subnet-route-table-association (s/keys :req [::route-table-id
                                                      ::subnet-id]))

(defresource subnet-route-table-association (ec2 "SubnetRouteTableAssociation") ::subnet-route-table-association)

(s/def ::eip-association (s/keys :opt [::allocation-id
                                       ::eip
                                       ::instance-id
                                       ::network-interface-id
                                       ::private-ip-address]))

(defresource eip-association (ec2 "EIPAssociation") ::eip-association)

(defresource internet-gateway (ec2 "InternetGateway") (s/keys :opt [::tags]))

(defresource nat-gateway (ec2 "NatGateway") (s/keys :req [::allocation-id ::subnet-id]
                                                         :opt [::tags]))

(s/def ::vpc-gateway-attachment (s/keys :req [::vpc-id]
                                        :opt [::internet-gateway-id
                                              ::vpn-gateway-id]))

(defresource vpc-gateway-attachment (ec2 "VPCGatewayAttachment") ::vpc-gateway-attachment)

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

(s/def ::security-group-ingress-embedded (s/* (s/keys :req [::ip-protocol]
                                             :opt [::cidr-ip
                                                   ::from-port
                                                   ::to-port
                                                   ::source-security-group-id
                                                   ::source-security-group-name
                                                   ::source-security-group-owner-id])))

(s/def ::destination-security-group-id ::security-group-id)

(s/def ::security-group-egress-embedded (s/* (s/keys :req [::ip-protocol]
                                            :opt [::from-port
                                                  ::to-port
                                                  ::destination-security-group-id])))

(s/def ::security-group (s/keys :req [::group-description]
                                :opt [::security-group-ingress-embedded
                                      ::security-group-egress-embedded
                                      ::res/tags
                                      ::vpc-id]))

(defresource security-group (ec2 "SecurityGroup") ::security-group)

(s/def ::security-group-ingress (s/keys :req [::ip-protocol]
                                             :opt [::cidr-ip
                                                   ::cidr-ipv6
                                                   ::description
                                                   ::from-port
                                                   ::to-port
                                                   ::group-id
                                                   ::group-name
                                                   ::source-security-group-id
                                                   ::source-security-group-name
                                                   ::source-security-group-owner-id]))

(defresource security-group-ingress (ec2 "SecurityGroupIngress") ::security-group-ingress)

(s/def ::security-group-egress (s/keys :req [::ip-protocol]
                                             :opt [::cidr-ip
                                                   ::cidr-ipv6
                                                   ::description
                                                   ::from-port
                                                   ::to-port
                                                   ::group-id
                                                   ::destination-prefix-list-id
                                                   ::destination-security-group-id]))

(defresource security-group-egress (ec2 "SecurityGroupEgress") ::security-group-egress)
