(ns crucible.aws.ec2-test
  (:require  [clojure.test :refer :all]
             [crucible.aws.ec2 :as ec2]
             [crucible.resources :as res]
             [cheshire.core :as json]
             [clojure.spec.alpha :as s]
             [crucible.core :refer [template encode parameter xref]]))

(deftest vpc-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/vpc {::ec2/cidr-block "1.2.3.4/24"}))))))

(deftest igw-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/internet-gateway {}))))))

(deftest nat-gateway-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/nat-gateway {::ec2/allocation-id "id"
                                                           ::ec2/subnet-id "id"})))))
  (testing "full spec"
    (is (s/valid? ::res/resource (second (ec2/nat-gateway {::ec2/allocation-id "id"
                                                           ::ec2/subnet-id "id"
                                                           ::ec2/tags [{::res/key "key" ::res/value "value"}]})))))
  (testing "template with multiple conditions"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"NatGateway" {"Type" "AWS::EC2::NatGateway"
                                       "Properties" {"AllocationId" "id"
                                                     "SubnetId" "id"}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :nat-gateway (ec2/nat-gateway {::ec2/allocation-id "id"
                                               ::ec2/subnet-id "id"}))))))))

(deftest route-table-test
  (testing "minimal spec"
    (is (s/valid? ::res/resource (second (ec2/route-table {::ec2/vpc-id "id"})))))
  (testing "full spec"
    (is (s/valid? ::res/resource (second (ec2/route-table {::ec2/vpc-id "id"
                                                           ::ec2/tags [{::res/key "key" ::res/value "value"}]})))))
  (testing "template with route table"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "t"
            "Resources" {"RouteTable" {"Type" "AWS::EC2::RouteTable"
                                       "Properties" {"VpcId" "id"}}}}
           (cheshire.core/decode
            (encode
             (template "t"
                       :route-table (ec2/route-table {::ec2/vpc-id "id"}))))))))

(deftest sg-test
  (testing "encode"
    (is (= {"AWSTemplateFormatVersion" "2010-09-09"
            "Description" "minimal"
            "Resources"
            {"MySecurityGroup"
             {"Type" "AWS::EC2::SecurityGroup"
              "Properties" {"GroupDescription"
                            "Enable SSH access and HTTP from the load balancer only"
                            "SecurityGroupIngress"
                            [{"IpProtocol" "tcp"
                              "FromPort" 22
                              "ToPort" 22
                              "CidrIp" "0.0.0.0/0"}
                             {"IpProtocol" "tcp"
                              "FromPort" { "Ref" "WebServerPort" }
                              "ToPort" { "Ref" "WebServerPort" }
                              "SourceSecurityGroupOwnerId" {"Fn::GetAtt"
                                                            ["ElasticLoadBalancer"
                                                             "SourceSecurityGroup.OwnerAlias"]}
                              "SourceSecurityGroupName" {"Fn::GetAtt"
                                                         ["ElasticLoadBalancer"
                                                          "SourceSecurityGroup.GroupName"]}}]}
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
