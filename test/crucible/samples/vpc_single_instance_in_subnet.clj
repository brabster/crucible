(ns crucible.samples.vpc-single-instance-in-subnet
  (:require [crucible.core :refer [template resource xref join]]
            [crucible.resources.aws.ec2 :as ec2]))

(defn network-acl-entry
  [rule-number from-port to-port & {:keys [egress]}]
  (ec2/network-acl-entry :network-acl-id (xref :network-acl)
                         :rule-number (str rule-number)
                         :protocol "6" ;;tcp
                         :rule-action "allow"
                         :egress (if egress "true" "false")
                         :cidr-block "0.0.0.0/0"
                         :port-range {:from (str from-port) :to (str to-port)}))

(def t (template
        :description "AWS CloudFormation Sample Template VPC_Single_Instance_In_Subnet: Sample template showing how to create a VPC and add an EC2 instance with an Elastic IP address and a security group. **WARNING** This template creates an Amazon EC2 instance. You will be billed for the AWS resources used if you create a stack from this template."
        :parameters
        {:instance-type {:description "WebServer EC2 instance type"
                         :type :string
                         :default-value "t2.small"
                         :constraint-description "must be a valid EC2 instance type."}
         :key-name {:description "Name of an existing EC2 KeyPair to enable SSH access to the instance"
                    :type :string
                    :constraint-description "must be the name of an existing EC2 KeyPair."}
         :ssh-location {:description "The IP address range that can be used to SSH to the EC2 instance"
                        :type :string
                        :min-length "9"
                        :max-length "18"
                        :default-value "0.0.0.0/0"
                        :allowed-pattern "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,2})"
                        :constraint-description "must be a valid IP CIDR of the form x.x.x.x/x."}}
        :resources
        {:vpc (resource (ec2/vpc :cidr-block "10.0.0.0/16"))
         :subnet (resource (ec2/subnet :cidr-block "10.0.0.0/24"))
         :internet-gateway (resource (ec2/internet-gateway))
         :attach-gateway (resource (ec2/vpc-gateway-attachment :vpc-id (xref :vpc)))
         :route-table (resource (ec2/route-table :vpc-id (xref :vpc)))
         :route (resource (ec2/route :route-table-id (xref :route-table)
                                     :destination-cidr-block "0.0.0.0/0"
                                     :gateway-id (xref :internet-gateway))
                          :depends-on (xref :attach-gateway))
         :subnet-route-table-association (resource (ec2/subnet-route-table-association
                                                    :subnet-id (xref :subnet)
                                                    :route-table-id (xref :route-table)))
         :network-acl (resource (ec2/network-acl :vpc-id (xref :vpc)))
         :inbound-http-network-acl-entry (resource (network-acl-entry 100 80 80))
         :inbound-ssh-network-acl-entry (resource (network-acl-entry 101 22 22))
         :inbound-response-ports-network-acl-entry (resource (network-acl-entry 102 1024 65535))
         :outbound-http-network-acl-entry (resource (network-acl-entry 100 80 80 :egress true))
         :outbound-https-network-acl-entry (resource (network-acl-entry 101 443 443 :egress true))
         :outbound-response-ports-network-acl-entry (resource (network-acl-entry 101 1024 65535 :egress true))
         :subnet-network-acl-association (resource (ec2/subnet-network-acl-association
                                                    :subnet-id (xref :subnet)
                                                    :network-acl-id (xref :network-acl)))
         :iP-address (resource (ec2/eip :domain "vpc" :instance-id (xref :web-server-instance))
                               :depends-on (xref :attach-gateway))
         :instance-security-group (resource
                                   (ec2/security-group
                                    :vpc-id (xref :vpc)
                                    :group-description "Enable SSH access via port 22"
                                    :security-group-ingress
                                    [{:ip-protocol "tcp"
                                      :from-port "22" :to-port "22" :cidr-ip (xref :ssh-location)}
                                     {:ip-protocol "tcp"
                                      :from-port "80" :to-port "80" :cidr-ip "0.0.0.0/0"}]))
         :web-server-instance (resource
                               (ec2/instance
                                :image-id "xyz"
                                :instance-type (xref :instance-type)
                                :key-name (xref :key-name))
                               :depends-on (xref :attach-gateway)
                               :creation-policy {:resource-signal {:timeout "PT15M"}})}
        :outputs {:URL (join ["http://" (xref :web-server-instance :public-ip)])}))
