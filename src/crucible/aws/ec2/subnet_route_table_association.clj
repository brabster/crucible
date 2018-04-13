(ns crucible.aws.ec2.subnet-route-table-association
  "AWS::EC2::SubnetRouteTableAssociation"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.ec2 :as ec2]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(s/def ::route-table-id (spec-or-ref string?))

(s/def ::subnet-id (spec-or-ref string?))

(s/def ::subnet-route-table-association (s/keys :req [::route-table-id
                                                      ::subnet-id]))

(defresource subnet-route-table-association (ec2/ec2 "SubnetRouteTableAssociation") ::subnet-route-table-association)
