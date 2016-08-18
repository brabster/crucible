(ns crucible.aws.ec2-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.ec2 :as ec2]
             [crucible.resources :as res]
             [cheshire.core :as json]
             [clojure.spec :as s]
             [crucible.core :as cru]))

(deftest vpc-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/vpc #::ec2{:cidr-block "1.2.3.4/24"}))))))

(deftest igw-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/internet-gateway {}))))))

(deftest sg-test
  (testing "encode"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "minimal"
            "Resources"
            {"MySecurityGroup"
             {"Type" "AWS::EC2::SecurityGroup"
              "Properties" {"GroupDescription" "Enable SSH access and HTTP from the load balancer only"
                            "SecurityGroupIngress"
                            [{"IpProtocol" "tcp"
                              "FromPort" 22
                              "ToPort" 22
                              "CidrIp" "0.0.0.0/0"}
                             {"IpProtocol" "tcp"
                              "FromPort" { "Ref" "WebServerPort" }
                              "ToPort" { "Ref" "WebServerPort" }
                              "SourceSecurityGroupOwnerId"
                              {"Fn::GetAtt" ["ElasticLoadBalancer" "SourceSecurityGroup.OwnerAlias"]}
                              "SourceSecurityGroupName"
                              {"Fn::GetAtt" ["ElasticLoadBalancer" "SourceSecurityGroup.GroupName"]}}]}
              }}
            "Parameters" {"ElasticLoadBalancer" {"Type" "String"}
                          "WebServerPort" {"Type" "String"}}}
           (json/decode
            (cru/encode
             (cru/template
              "minimal"
              :elastic-load-balancer (cru/parameter)
              :web-server-port (cru/parameter)
              :my-security-group
              (ec2/security-group
               #::ec2{:group-description "Enable SSH access and HTTP from the load balancer only"
                      :security-group-ingress
                      [#::ec2{:ip-protocol "tcp"
                              :from-port 22
                              :to-port 22
                              :cidr-ip "0.0.0.0/0"}
                       #::ec2{:ip-protocol "tcp"
                              :from-port (cru/xref :web-server-port)
                              :to-port (cru/xref :web-server-port)
                              :source-security-group-owner-id
                              (cru/xref :elastic-load-balancer (keyword "SourceSecurityGroup.OwnerAlias"))
                              :source-security-group-name
                              (cru/xref :elastic-load-balancer (keyword "SourceSecurityGroup.GroupName"))}]}))))))))
