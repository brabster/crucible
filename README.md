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
repl> (clojure.pprint/pprint (encode simple))
{"AWSTemplateFormatVersion" "2010-09-09"
 "Description" "A simple sample template"
 "Parameters" {"MyVpcCidr" {"Type" "String"}}
 "Resources" {"MyVpc"
              {"Type" "AWS::EC2::VPC",
               "Properties" {"CidrBlock" {"Ref" "MyVpcCidr"}}}},
 "Outputs" {"Vpc" {"Value" {"Fn::Join" ["/" ["foo" {"Ref" "MyVpc"}]]}}}}
```

Alternative template construction function accepts map and string arguments for building a template from partials, for example:

```clojure
(def simple (-> {:my-vpc-cidr (parameter)}
                (assoc :igw (ec2/internet-gateway {}))
                (assoc :my-vpc (ec2/vpc {::ec2/cidr-block (xref :my-vpc-cidr)}))
                (assoc :vpc (output (join "/" ["foo" (xref :my-vpc)])))
                (template "A simple sample template")))
```

### Parameter Options

See `crucible.parameters` namespace, required as `param` in this example:

```clojure
:my-vpc-cidr (parameter ::param/type ::param/number
                        ::param/description "A demonstration of parameter options"
                        ::param/allowed-values [1 2 3]
                        ::param/no-echo true)
```

### Resource Policies

See `crucible.policies` namespace, required as `policies` in this example:

```clojure
:my-vpc (ec2/vpc {::ec2/cidr-block (xref :my-vpc-cidr)}
                 (policies/deletion ::policies/retain)
                 (policies/depends-on :my-vpc-cidr))
```

## Resource Types

Standard AWS resource types can be found as children of the [crucible.aws](src/crucible/aws) namespace.

Examples of resource type usage can be found in the tests.

* AWS::EC2::* partial coverage
* AWS::ElasticLoadBalancingV2::*
* AWS::ApiGateway::*
* AWS::DynamoDB::Table
* AWS::CloudWatch::Alarm
* AWS::Lambda::Function
* AWS::Lambda::EventSourceMapping
* AWS::IAM::Role (basic support for Lambda applications)
* AWS::S3::Bucket
* AWS::CloudFormation::Stack
* AWS::Kinesis::Stream
* AWS::KinesisFirehose::DeliveryStream
* AWS::SNS::Topic
* AWS::SNS::TopicPolicy
* AWS::Events::Rule
* Custom::* custom resources

## Writing your own resource type

The easiest way is to use `defresource` and `spec-or-ref` from the `crucible.resources` namespace, eg.

```clojure
(ns crucible.aws.ec2
  "Resources in AWS::EC2::*"
  (:require [crucible.resources :refer [spec-or-ref defresource] :as res]
            [clojure.spec :as s]))

;; spec-or-ref applies your spec if a literal value is given,
;; but also allows a parameter or function to be given instead of a literal.
(s/def ::cidr-block (spec-or-ref string?))

;; ::res/tags reuses the tags spec defined in the crucible/resources namespace
(s/def ::vpc (s/keys :req [::cidr-block]
                     :opt [::enable-dns-support
                           ::enable-dns-hostnames
                           ::instance-tenancy
                           ::res/tags]))

;; creates resource factory crucible.aws.ec2/vpc with type "AWS::EC2::VPC" 
;; and validates the data structure using the ::vpc spec
(defresource  vpc "AWS::EC2::VPC" ::vpc)
```

Pull requests to add or enhance resource types available in Crucible will be welcomed. If it's a standard AWS type please place it in the `crucible.aws` namespace and use `defresource` as it documents the resource type for you. At lest one test for the update would be great!

### Testing a Resource Type

Although you can test a resource by testing the data structure and validity directly, testing the conversion from the crucible code to a map ready to encode as CloudFormation-valid JSON is good minimal coverage. The default `clojure.test` behaviour does not pretty-print the `ex-data` map on validation exceptions, which makes testing and debugging validation failures painful. A custom assertion `crucible.assertion/resource=` is available to ensure any failure map is pretty-printed. See [the resource tests](/test/crucible/aws) for examples.

## Overriding JSON Keys

Crucible uses camel-snake-kebab's `->PascalCase` function to convert
Clojure map keys into JSON map keys. That takes care of most
translations between Clojure-style `:keyword-key` and
JSON/CloudFormation-style `KeywordKey`. To handle the occasional
mistranslation, typically due to capitalisation,
`clojure.encoding.keys` exposes a `->key` multimethod, allowing
overriding of the translation. For example, this problem occurs in
AWS::CloudFormation::Stack, where a required key is `TemplateURL`. The
following overrides the natural translation of `:template-url` to
`TemplateUrl`.

```clojure

(ns crucible.aws.cloudformation
  (:require [crucible.encoding.keys :refer [->key]]))

(defmethod ->key :template-url [_] "TemplateURL")
```

Note, these translations take place during the final JSON encoding step and do not see keyword namespacing.

## CLI Support

Basic CLI support, intended for use with Leiningen, is provided in the `crucible.encoding.main/-main` function. Running this function will reload the namespaces available in the project, then enumerate any vars that have a metadata tag provided by the `crucible.core/template` function. These vars are then encoded into CloudFormation templates and exported to the local filesystem. They can then be used directly or uploaded to S3 for use with CloudFormation.

Flag `-h` for help. Templates are exported to `target/templates` by default, override with `-o output-dir`. Namespaces are converted to filesystem locations by replacing `.` characters with `/` characters.

I create a templates directory within my project and then add it as a source-path and crucible as a dependency to the dev profile. Then I can work at the repl, write tests for my templates and use this tooling without having my template code or crucible mixed with my source code.

[demo-project](demo-project) is an example of setting up a project with crucible templates defined alongside the code. The templates can see the code to verify any references they might have, but not the other way round. Run `lein templates` in the demo project to generate templates.

```clojure
:aliases {"templates" ["run" "-m" crucible.encoding.main]} 
:profiles {:dev {:source-paths ["templates"]
                 :dependencies [[crucible "0.10.0-SNAPSHOT"]]}}
```

## Helping Out

Any help appreciated! Happy to receive any issues and pull requests. See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php)
