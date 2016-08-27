(ns crucible.aws.ec2-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.ec2 :as ec2]
             [crucible.resources :as res]
             [cheshire.core :as json]
             [clojure.spec :as s]
             [crucible.core :refer [template encode parameter xref]]))

(deftest vpc-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/vpc {::ec2/cidr-block "1.2.3.4/24"}))))))

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
            (encode
             (template
              "minimal"
              :elastic-load-balancer (parameter)
              :web-server-port (parameter)
              :my-security-group
              (ec2/security-group
               {::ec2/group-description "Enable SSH access and HTTP from the load balancer only"
                ::ec2/security-group-ingress
                [{::ec2/ip-protocol "tcp"
                  ::ec2/from-port 22
                  ::ec2/to-port 22
                  ::ec2/cidr-ip "0.0.0.0/0"}
                 {::ec2/ip-protocol "tcp"
                  ::ec2/from-port (xref :web-server-port)
                  ::ec2/to-port (xref :web-server-port)
                  ::ec2/source-security-group-owner-id
                  (xref :elastic-load-balancer (keyword "SourceSecurityGroup.OwnerAlias"))
                  :source-security-group-name
                  (xref :elastic-load-balancer (keyword "SourceSecurityGroup.GroupName"))}]}))))))))
