(ns crucible.aws.route53
  "Resources in AWS::Route53::*")

(defn prefix [suffix] (str "AWS::Route53::" suffix))
