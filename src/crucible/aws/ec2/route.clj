(ns crucible.aws.ec2.route
  "AWS::EC2::Route"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.ec2 :as ec2]))

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

(defresource route (ec2/ec2 "Route") ::route)
