(ns crucible.aws.iam-test
  (:require [crucible.aws.iam :as iam]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]
            [crucible.core :refer [xref] :as cf]))

(defn valid [spec candidate]
  (nil? (s/explain-data spec candidate)))

(deftest principal-tests

  (testing "everyone via *" (is (valid ::iam/principal "*")))

  (testing "everyone via {AWS *}" (is (valid ::iam/principal {::iam/aws "*"})))

  (testing "aws account" (is (valid ::iam/principal {::iam/aws "foo"})))

  (testing "list of aws accounts" (is (valid ::iam/principal {::iam/aws ["foo" "bar"]})))

  (testing "federated identity" (is (valid ::iam/principal {::iam/federated "graph.facebook.com"})))

  (testing "federated identity single"
    (is (valid ::iam/principal {::iam/service "ec2.amazonaws.com"})))

  (testing "federated identity"
    (is (valid ::iam/principal {::iam/service ["ec2.amazonaws.com"
                                               "datapipeline.amazonaws.com"]})))

  (testing "federated identity string"
    (is (valid ::iam/principal {"Service" "ec2.amazonaws.com"})))

  (testing "canonical user" (is (valid ::iam/principal {::iam/canonical-user "foo"})))

  (testing "not principal"
    (is (valid ::iam/not-principal {::iam/service ["ec2.amazonaws.com"
                                                   "datapipeline.amazonaws.com"]}))))

(deftest action-tests

  (testing "hyphen in action" (is (valid ::iam/action "execute-api:Invoke")))

  (testing "all actions" (is (valid ::iam/action "*")))

  (testing "single action" (is (valid ::iam/action "s3:PutObject")))

  (testing "multiple actions" (is (valid ::iam/action ["s3:PutObject" "s3:DeleteObject"])))

  (testing "not action" (is (valid ::iam/not-action "s3:PutObject"))))

(deftest resource-tests

  (testing "all resources" (is (valid ::iam/resource "*")))

  (testing "single resource" (is (valid ::iam/resource "foo")))

  (testing "multiple resources" (is (valid ::iam/resource ["foo" "bar"])))

  (testing "not resource" (is (valid ::iam/not-resource "foo"))))

(deftest role-tests

  (testing "simple role"
    (is (valid ::iam/role
               {::iam/assume-role-policy-document
                {::iam/version "2012-10-17"
                 ::iam/statement [{::iam/effect "Allow"
                                   ::iam/principal {::iam/service ["ecs-tasks.amazonaws.com"]}
                                   ::iam/action ["sts:AssumeRole"]}]}}))))

(deftest condition-tests

  (testing "single condition"
    (is (valid ::iam/condition {:date-greater-than
                                {"aws:CurrentTime" "2013-08-16T12:00:00Z"}})))

  (testing "single condition string"
    (is (valid ::iam/condition {"DateGreaterThan"
                                {"aws:CurrentTime" "2013-08-16T12:00:00Z"}}))))

(deftest user-tests
  (testing "empty user"
    (is (valid ::iam/user {}))))

(deftest policy-tests
  (testing "attaching a policy to a user"
    (is (valid ::iam/policy {::iam/policy-name "db-access"
                             ::iam/users [(xref :user1) (xref :user2)]
                             ::iam/policy-document {::iam/version "2012-10-17"
                                                    ::iam/statement [{::iam/action ["dynamodb:*"]
                                                                      ::iam/effect "Allow"
                                                                      ::iam/resource "arn:aws:dynamodb:*:..."}]}})))
  (testing "attaching a policy to a group"
    (is (valid ::iam/policy {::iam/policy-name "db-access"
                             ::iam/groups [(xref :group1) (xref :group2)]
                             ::iam/policy-document {::iam/version "2012-10-17"
                                                    ::iam/statement [{::iam/action ["dynamodb:*"]
                                                                      ::iam/effect "Allow"
                                                                      ::iam/resource "arn:aws:dynamodb:*:..."}]}})))
  (testing "attaching a policy to a role"
    (is (valid ::iam/policy {::iam/policy-name "db-access"
                             ::iam/roles [(xref :transactor-role) (xref :backup-transactor-role)]
                             ::iam/policy-document {::iam/version "2012-10-17"
                                                    ::iam/statement [{::iam/action ["dynamodb:*"]
                                                                      ::iam/effect "Allow"
                                                                      ::iam/resource "arn:aws:dynamodb:*:..."}]}}))))
