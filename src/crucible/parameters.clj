(ns crucible.parameters
  (:require [clojure.spec :as s]
            [crucible.encoding.keys :as keys]))

(defmethod keys/->key :list-number [_] "List<Number>")
(defmethod keys/->key :aws-ec2-az-name [_] "AWS::EC2::AvailabilityZone::Name")
(defmethod keys/->key :aws-ec2-image-id [_] "AWS::EC2::Image::Id")
(defmethod keys/->key :aws-ec2-instance-id [_] "AWS::EC2::Instance::Id")

(s/def ::type #{::string
                ::number
                ::comma-delimited-list
                ::list-number
                ::aws-ec2-az-name
                ::aws-ec2-image-id
                ::aws-ec2-instance-id})

(s/def ::description string?)

(s/def ::constraint-description string?)

(s/def ::allowed-values (s/+ string?))

(s/def ::allowed-pattern string?)

(s/def ::default string?)

(s/def ::no-echo #{true})

(s/def ::max-value (s/and number? pos?))

(s/def ::min-value ::max-value)

(s/def ::max-length (s/and integer? pos?))

(s/def ::min-length ::max-length)

(s/def ::parameter
  (s/keys :req [::type]
          :opt [::description
                ::allowed-values
                ::allowed-pattern
                ::constraint-description
                ::default
                ::no-echo
                ::min-value
                ::max-value
                ::min-length
                ::max-length]))
