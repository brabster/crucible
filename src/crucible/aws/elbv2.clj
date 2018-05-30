(ns crucible.aws.elbv2
  "Resources in AWS::ElasticLoadBalancingV2::*")

(defn prefix [suffix] (str "AWS::ElasticLoadBalancingV2::" suffix))
