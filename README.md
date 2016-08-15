# crucible

Create better cloudformation templates with Clojure

[![Travis Build](https://travis-ci.org/brabster/crucible.svg?branch=master)](https://travis-ci.org/brabster/crucible)

## Installation

Crucible depends on clojure.spec, currently available in Clojure 1.9 alpha 10+ (breaking changes in spec around alpha 9)

0.9.2-SNAPSHOT currently includes the latest changes.

![](https://clojars.org/crucible/latest-version.svg)

The next major release will assemble a more convenient API, so may involve breaking changes.

## Examples

```clojure
(ns crucible.examples-test
  (:require [crucible.core :refer [template parameter resource output xref encode join]]
            [crucible.aws.ec2 :as ec2]))

(def simple (template "A simple sample template"
                      :my-vpc-cidr (parameter)
                      :my-vpc (ec2/vpc {::ec2/cidr-block (xref :my-vpc-cidr)})
                      :vpc (output (join "/" ["foo" (xref :my-vpc)]))))

```

```clojure
repl> (clojure.pprint/pprint simple)
{"AWSTemplateFormatVersion" "2010-09-09"
 "Description" "A simple sample template"
 "Parameters" {"MyVpcCidr" {"Type" "String"}}
 "Resources" {"MyVpc"
              {"Type" "AWS::EC2::VPC",
               "Properties" {"CidrBlock" {"Ref" "MyVpcCidr"}}}},
 "Outputs" {"Vpc" {"Value" {"Fn::Join" ["/" ["foo" {"Ref" "MyVpc"}]]}}}}
```

## Resource Types

* AWS::EC2::* partial coverage
* AWS::DynamoDB::Table
* AWS::CloudWatch::Alarm
* AWS::Lambda::Function
* AWS::Lambda::EventSourceMapping
* AWS::IAM::Role (basic support for Lambda applications)
* AWS::S3::Bucket
* AWS::CloudFormation::Stack
* AWS::Kinesis::Stream
* Custom::* custom resources

## Writing your own resource type

The quickest way is to use `crucible.resources/resource-factory`, eg.

```clojure
(ns crucible.aws.ec2
  (:require [clojure.spec :as s]
            [crucible.resources :as r]
            [crucible.values :as v]))

(s/def ::cidr-block ::v/value)

(s/def ::vpc (s/keys :req [::cidr-block]
                     :opt [::enable-dns-support
                           ::enable-dns-hostnames
                           ::instance-tenancy
                           ::r/tags]))

(def vpc (r/resource-factory "AWS::EC2::VPC" ::vpc))
```

## Overriding JSON Keys

Crucible uses camel-snake-kebab's `->PascalCase` function to convert Clojure map keys into JSON map keys. That takes care of most translations between Clojure-style `:keyword-key` and JSON/CloudFormation-style `KeywordKey`. To handle the occasional mistranslation, typically due to capitalisation, `clojure.encoding.keys` exposes a `->key` multimethod, allowing overriding of the translation. For example, this problem occurs in AWS::CloudFormation::Stack, where a required key is "TemplateURL". The following overrides the natural translation of :template-url -> TemplateUrl.

```clojure

(ns crucible.aws.cloudformation
  (:require [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :template-url [_] "TemplateURL")
```

Note, these translations take place during the final JSON encoding step and do not see keyword namespacing.

## Helping Out

Any help appreciated! Happy to receive any issues, pull requests, etc.

## License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php)
