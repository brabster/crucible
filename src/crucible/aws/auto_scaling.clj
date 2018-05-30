(ns crucible.aws.auto-scaling
  "Resources in AWS::AutoScaling::*")

(defn autoscaling [suffix] (str "AWS::AutoScaling::" suffix))
