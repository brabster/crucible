# crucible

Create better cloudformation templates with Clojure

[![Travis Build](https://travis-ci.org/brabster/crucible.svg?branch=master)](https://travis-ci.org/brabster/crucible) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/290/badge)](https://bestpractices.coreinfrastructure.org/projects/290)

## Installation

Crucible depends on clojure.spec, currently available in Clojure 1.9 alpha 10+ (breaking changes in spec around alpha 9)

![Clojars Latest Version](https://clojars.org/crucible/latest-version.svg)

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

### Parameter Options

See `crucible.parameters` namespace, required as `param` in this example:

```clojure
:my-vpc-cidr (parameter ::param/type ::param/string
                        ::param/allowed-values ["10.0.0.0/24" "10.0.0.0/16"])
```

### Resource Policies

See `crucible.policies` namespace, required as `policies` in this example:

```clojure
:my-vpc (ec2/vpc {::ec2/cidr-block (xref :my-vpc-cidr)}
                 (policies/deletion ::policies/retain)
                 (policies/depends-on :my-vpc-cidr))
```

## Resource Types

Examples of resource type usage can be found in the tests.

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

Crucible uses camel-snake-kebab's `->PascalCase` function to convert Clojure map keys into JSON map keys. That takes care of most translations between Clojure-style `:keyword-key` and JSON/CloudFormation-style `KeywordKey`. To handle the occasional mistranslation, typically due to capitalisation, `clojure.encoding.keys` exposes a `->key` multimethod, allowing overriding of the translation. For example, this problem occurs in AWS::CloudFormation::Stack, where a required key is `TemplateURL`. The following overrides the natural translation of `:template-url` to `TemplateUrl`.

```clojure

(ns crucible.aws.cloudformation
  (:require [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :template-url [_] "TemplateURL")
```

Note, these translations take place during the final JSON encoding step and do not see keyword namespacing.

## CLI Support

Basic CLI support, intended for use with Leiningen, is provided in the `crucible.encoding.main/-main` function. Running this function will reload the namespaces available in the project, then enumerate any vars that have a metadata tag provided by the `crucible.core/template` function. These vars are then encoded into CloudFormation templates and exported to the local filesystem. They can then be used directly or uploaded to S3 for use with CloudFormation.

Flag `-h` for help. Templates are exported to `target/templates` by default, override with `-o output-dir`. Namespaces are converted to filesystem locations by replacing `.` characters with `/` characters.

An example of use with Leiningen aliases:

```clojure
:aliases {"build-template" ["run" "-m" crucible.encoding.main]} 
```

## Helping Out

Any help appreciated! Happy to receive any issues, pull requests, etc.

## License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php)
