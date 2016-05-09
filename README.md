# crucible

Create better cloudformation templates with Clojure

[![Travis Build](https://travis-ci.org/brabster/crucible.svg?branch=master)](https://travis-ci.org/brabster/crucible)

## Installation

[![Clojars Project](https://img.shields.io/clojars/v/crucible.svg)](https://clojars.org/crucible)

## Examples

```clojure
(ns crucible.samples.ex1
  (:require [crucible.core :refer [template resource xref join]]
            [crucible.resources.aws.ec2 :as ec2]))

(def simple (template :description "A simple sample template"
                      :parameters {:my-vpc-cidr {:type :string}}
                      :resources {:my-vpc (resource (ec2/vpc :cidr-block (xref :my-vpc-cidr)))}
                      :outputs {:vpc (join "/" ["foo" (xref :my-vpc)])}))


```

```clojure
repl> (clojure.pprint/pprint simple)
{"AWSTemplateFormatVersion" "2010-09-09",
 "Parameters" {"MyVpcCidr" {"Type" "String"}},
 "Resources"
 {"MyVpc"
  {"Type" "AWS::EC2::VPC",
   "Properties" {"CidrBlock" {"Ref" "MyVpcCidr"}}}},
 "Outputs" {"Vpc" {"Value" {"Fn::Join" ["/" ["foo" {"Ref" "MyVpc"}]]}}}}
```

See more complex sample in https://github.com/brabster/crucible/blob/master/test/crucible/samples/vpc_single_instance_in_subnet.clj

## License

Distributed under the [[http://opensource.org/licenses/eclipse-1.0.php][Eclipse Public License]],
