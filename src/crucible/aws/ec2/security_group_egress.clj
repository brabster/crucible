(ns crucible.aws.ec2.security-group-egress
  "AWS::EC2::SecurityGroupEgress"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.ec2 :as ec2]))

(s/def ::security-group-egress (s/keys :req [::ip-protocol]
                                       :opt [::cidr-ip
                                             ::cidr-ipv6
                                             ::description
                                             ::from-port
                                             ::to-port
                                             ::group-id
                                             ::destination-prefix-list-id
                                             ::destination-security-group-id]))

(defresource security-group-egress (ec2/ec2 "SecurityGroupEgress") ::security-group-egress)
