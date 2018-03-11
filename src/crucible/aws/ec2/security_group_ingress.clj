(ns crucible.aws.ec2.security-group-ingress
  "AWS::EC2::SecurityGroupIngress"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.ec2 :as ec2]))

(s/def ::ip-protocol (spec-or-ref (s/or :int (s/and integer?
                                                    #(<= -1 %))
                                        :str #{"tcp" "udp" "icmp"})))

(s/def ::cidr-ip (spec-or-ref string?))

(s/def ::cidr-ipv6 (spec-or-ref string?))

(s/def ::description (spec-or-ref string?))

(s/def ::from-port ::ec2/port)

(s/def ::to-port ::ec2/port)

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

(defresource security-group-ingress (ec2/ec2 "SecurityGroupIngress") ::security-group-ingress)
