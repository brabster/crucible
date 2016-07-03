# crucible

Create better cloudformation templates with Clojure

[![Travis Build](https://travis-ci.org/brabster/crucible.svg?branch=master)](https://travis-ci.org/brabster/crucible)

## Installation

Crucible depends on clojure.spec, currently available in Clojure 1.9 alpha 4+

![](https://clojars.org/crucible/latest-version.svg)

## Examples

```clojure
(ns crucible.examples-test
  (:require  [clojure.test :refer :all]
             [crucible.template :refer [template parameter resource output xref encode]]
             [crucible.values :refer [join]]
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

## License

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php)
