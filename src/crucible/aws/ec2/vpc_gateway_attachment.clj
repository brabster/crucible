(ns crucible.aws.ec2.vpc-gateway-attachment
  "AWS::EC2::VPCGatewayAttachment"
  (:require [clojure.spec.alpha :as s]
            [crucible.resources :refer [spec-or-ref defresource]]
            [crucible.aws.ec2 :as ec2]))

(s/def ::vpc-id ::ec2/vpc-id)
(s/def ::internet-gateway-id (spec-or-ref string?))
(s/def ::vpn-gateway-id (spec-or-ref string?))

(s/def ::vpc-gateway-attachment (s/keys :req [::vpc-id]
                                        :opt [::internet-gateway-id
                                              ::vpn-gateway-id]))

(defresource vpc-gateway-attachment (ec2/ec2 "VPCGatewayAttachment") ::vpc-gateway-attachment)
