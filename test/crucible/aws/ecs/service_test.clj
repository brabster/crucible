(ns crucible.aws.ecs.service-test
  (:require [crucible.aws.ecs :as ecs]
            [crucible.aws.ecs.service :as service]
            [crucible.core :refer [xref]]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest service-tests

  (testing "valid service"
    (is
     (s/valid? ::service/service
               {::service/service-name "https-redirect-v2"
                ::service/platform-version "1.1.0"
                ::service/task-definition (xref :https-redirect-task)
                ::service/cluster (xref :cluster)
                ::service/launch-type "FARGATE"
                ::service/network-configuration {::service/aws-vpc-configuration
                                                 {::service/subnets [(xref :private1)
                                                                     (xref :private2)]
                                                  ::service/security-groups [(xref :sg-private)
                                                                             (xref :sg-https-redirect)]
                                                  ::service/assign-public-ip "DISABLED"} }
                ::service/load-balancers [{::service/container-name "https-redirect"
                                           ::service/container-port 80
                                           ::service/target-group-arn (xref :http-target-group)}]
                ::service/desired-count 1}))))
