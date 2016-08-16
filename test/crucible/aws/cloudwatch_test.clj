(ns crucible.aws.cloudwatch-test
  (:require [crucible.aws.cloudwatch :as cw]
            [crucible.core :as cru]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest minimal-cloudwatch-test
  (testing "encode"
    (is (= (json/decode (slurp (io/resource "aws/cloudwatch/alarm.json")))
           (json/decode
            (cru/encode
             (cru/template
              "sample"
              :web-server-scale-up-policy (cru/parameter)
              :web-server-group (cru/parameter)
              :cpu-alarm-high (cw/alarm #::cw{:alarm-description "Scale-up if CPU is greater than 90% for 10 minutes"
                                              :metric-name "CPUUtilization"
                                              :namespace "AWS/EC2"
                                              :statistic "Average"
                                              :period "300"
                                              :evaluation-periods "2"
                                              :threshold "90"
                                              :alarm-actions [(cru/xref :web-server-scale-up-policy)]
                                              :dimensions [#::cw{:name "AutoScalingGroupName"
                                                                 :value (cru/xref :web-server-group)}]
                                              :comparison-operator "GreaterThanThreshold"}))))))))
