(ns crucible.aws.cloudwatch-test
  (:require [crucible.aws.cloudwatch :as cw]
            [crucible.core :refer [parameter xref]]
            [crucible.encoding :refer [rewrite-element-data]]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest minimal-cloudwatch-test
  (testing "encode"
    (is (= (json/decode (slurp (io/resource "aws/cloudwatch/alarm.json")))
           (rewrite-element-data
            (cw/alarm {::cw/alarm-description "Scale-up if CPU is greater than 90% for 10 minutes"
                       ::cw/metric-name "CPUUtilization"
                       ::cw/namespace "AWS/EC2"
                       ::cw/statistic "Average"
                       ::cw/period "300"
                       ::cw/evaluation-periods "2"
                       ::cw/threshold "90"
                       ::cw/alarm-actions [(xref :web-server-scale-up-policy)]
                       ::cw/dimensions [{::cw/name "AutoScalingGroupName"
                                         ::cw/value (xref :web-server-group)}]
                       ::cw/comparison-operator "GreaterThanThreshold"}))))))
