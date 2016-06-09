# crucible

Create better cloudformation templates with Clojure

[![Travis Build](https://travis-ci.org/brabster/crucible.svg?branch=master)](https://travis-ci.org/brabster/crucible)

## Installation

![](https://clojars.org/crucible/latest-version.svg)

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

## Next Steps

I don't like the repetition when writing a template, eg `:resources {:foo (resource`. I'm working on an update to avoid the parameters/resources/outputs partitioning, deriving the type and final partition of the JSON output from the structure of the element. This is likely to be a breaking change, the resulting API is intended to change the example above to the following, whilst producing the same JSON output:

```clojure
(ns crucible.samples.ex1
  (:require [crucible.core :refer [template resource parameter output xref join]]
            [crucible.resources.aws.ec2 :as ec2]))

(def simple (template :description "A simple sample template"
                      :my-vpc-cidr (parameter {:type :string})
                      :my-vpc (ec2/vpc :cidr-block (xref :my-vpc-cidr))
                      :vpc (output (join "/" ["foo" (xref :my-vpc)]))))
```
I'd also like to make it easier to implement resource definitions without needing to modify this project, and I'd like to add `clojure.spec` to ensure that the user-provided data is valid before encoding. Current state of work is on [feature/rewrite-with-spec](/brabster/crucible/tree/feature/rewrite-with-spec).

## License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php)
