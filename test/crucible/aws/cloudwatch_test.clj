(ns crucible.aws.cloudwatch-test
  (:require [crucible.aws.cloudwatch :as cw]
            [crucible.template :as t]
            [crucible.encoding :as enc]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest minimal-cloudwatch-test
  (testing "encode"
    (is (= (json/decode (slurp (io/resource "aws/cloudwatch/alarm.json")))
           (json/decode
            (enc/encode
             (t/template
              "sample"
              :cpu-alarm-high (cw/alarm #::cw{:alarm-description "Scale-up if CPU is greater than 90% for 10 minutes"
                                              :metric-name "CPUUtilization"
                                              :namespace "AWS/EC2"
                                              :statistic "Average"
                                              :period "300"
                                              :evaluation-periods "2"
                                              :threshold "90"
                                              :alarm-actions [(t/xref :web-server-scale-up-policy)]
                                              :dimensions [#::cw{:name "AutoScalingGroupName"
                                                                 :value (t/xref :web-server-group)}]
                                              :comparison-operator "GreaterThanThreshold"}))))))))
