(ns crucible.resources.aws.ec2
  (:require [crucible.resources :refer [generic-resource]]))

(defn eip
  [& {:keys [instance-id domain] :as props}]
  (generic-resource "AWS::EC2::EIP" props))

(defn eip-association
  [& {:keys [allocation-id eip instance-id network-interface-id private-ip-address] :as props}]
  (generic-resource "AWS::EC2::EIPAssociation" props))

(defn internet-gateway
  [& {:keys [tags] :as props}]
  (generic-resource "AWS::EC2::InternetGateway" props))

(defn vpc-gateway-attachment
  [& {:keys [vpc-id internet-gateway-id] :as props}]
  (generic-resource "AWS::EC2::VPCGatewayAttachment" props))

(defn nat-gateway
  [& {:keys [allocation-id subnet-id] :as props}]
  (generic-resource "AWS::EC2::NatGateway" props))

(defn vpc
  [& {:keys [cidr-block enable-dns-support enable-dns-hostnames instance-tenancy tags] :as props}]
  (generic-resource "AWS::EC2::VPC" props))

(defn route-table
  [& {:keys [vpc-id] :as props}]
  (generic-resource "AWS::EC2::RouteTable" props))

(defn route
  [& {:keys [route-table-id destination-cidr-block gateway-id] :as props}]
  (generic-resource "AWS::EC2::Route" props))

(defn subnet-route-table-association
  [& {:keys [subnet-id route-table-id] :as props}]
  (generic-resource "AWS::EC2::SubnetRouteTableAssociation" props))

(defn network-acl
  [& {:keys [vpc-id] :as props}]
  (generic-resource "AWS::EC2::NetworkAcl" props))

(defn network-acl-entry
  [& {:keys [network-acl-id rule-number protocol rule-action egress cidr-block port-range] 
      :as props}]
  (generic-resource "AWS::EC2::NetworkAclEntry" props))

(defn subnet-network-acl-association
  [& {:keys [subnet-id network-acl-id] :as props}]
  (generic-resource "AWS::EC2::SubnetNetworkAclAssociation" props))

(defn security-group
  [& {:keys [vpc-id group-description security-group-ingress] :as props}]
  (generic-resource "AWS::EC2::SecurityGroup" props))

(defn subnet
  [& {:keys [availability-zone cidr-block map-public-ip-on-launch tags vpc-id] :as props}]
  (generic-resource "AWS::EC2::Subnet" props))

(defn instance
  [& {:keys [image-id instance-type key-name network-interfaces user-data] :as props}]
  (generic-resource "AWS::EC2::Instance" props))
