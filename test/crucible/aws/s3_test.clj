(ns crucible.aws.s3-test
  (:require [crucible.aws.s3 :as s3]
            [crucible.template :as t]
            [crucible.encoding :as enc]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest s3-cors-test
  (testing "encode"
    (is (= (json/decode (slurp (io/resource "aws/s3/s3-cors.json")))
           (json/decode
            (enc/encode
             (t/template
              "sample"
              :bucket
              (s3/bucket
               #::s3 {:access-control "PublicReadWrite"
                      :cors-configuration #::s3 {:cors-rules
                                                 [#::s3 {:allowed-headers ["*"]
                                                         :allowed-methods ["GET"]
                                                         :allowed-origins ["*"]
                                                         :exposed-headers ["Date"]
                                                         :id "myCORSRuleId1"
                                                         :max-age 3600}
                                                  #::s3 {:allowed-headers ["x-amz-*"]
                                                         :allowed-methods ["DELETE"]
                                                         :allowed-origins ["http://www.example1.com"
                                                                           "http://www.example2.com"]
                                                         :exposed-headers ["Connection"
                                                                           "Server"
                                                                           "Date"]
                                                         :id "myCORSRuleId2"
                                                         :max-age 1800}]}}))))))))
