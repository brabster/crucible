(ns crucible.aws.ec2.route-table
  "AWS::EC2::RouteTable"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.ec2 :as ec2]
            [crucible.resources :refer [spec-or-ref defresource] :as res]))

(s/def ::vpc-id ::ec2/vpc-id)

(s/def ::route-table (s/keys :req [::vpc-id]
                             :opt [::res/tags]))

(defresource route-table (ec2/ec2 "RouteTable") ::route-table)
