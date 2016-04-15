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

(defn nat-gateway
  [& {:keys [allocation-id subnet-id] :as props}]
  (generic-resource "AWS::EC2::NatGateway" props))

(defn vpc
  [& {:keys [cidr-block enable-dns-support enable-dns-hostnames instance-tenancy tags] :as props}]
  (generic-resource "AWS::EC2::VPC" props))

(defn subnet
  [& {:keys [availability-zone cidr-block map-public-ip-on-launch tags vpc-id] :as props}]
  (generic-resource "AWS::EC2::Subnet" props))
