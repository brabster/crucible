(ns crucible.aws.cloudwatch.anomaly-detector-test
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]
            [crucible.assertion :refer [resource=]]
            [crucible.aws.cloudwatch.anomaly-detector :as ad]))

(deftest anomaly-detection-test
  (testing "encode"
    (is (resource=
         (json/decode (slurp (io/resource "aws/cloudwatch/anomaly_detector.json")))
         (ad/anomaly-detector
          {::ad/namespace "AWSSDK/Java"
           ::ad/stat "Average"
           ::ad/metric-name "JvmMetric"
           ::ad/configuration
           {::ad/excluded-time-ranges
            [{::ad/end-time "2019-07-01T23:59:59"
              ::ad/start-time "2019-07-01T23:59:59"}]
            ::ad/metric-time-zone "America/New_York"}
           ::ad/dimensions [{::ad/name "Memory"
                             ::ad/value "UsedMemory"}]})))))
