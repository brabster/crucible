(ns crucible.aws.neptune.db-instance-test
  (:require [crucible.aws.neptune.db-instance :as dbi]
            [crucible.resources :as r]
            [clojure.test :refer :all]))

(deftest neptune-instance-test
  (testing "encode"
    (is (= {"Type" "AWS::Neptune::DBInstance",
            "Properties"
            {"AllowMajorVersionUpgrade" false,
             "AutoMinorVersionUpgrade" true,
             "DBInstanceClass" "db.r4.large",
             "Tags" [{"Key" "key", "Value" "value"}]}}
           (crucible.encoding/rewrite-element-data
            (dbi/db-instance {::dbi/allow-major-version-upgrade false
                              ::dbi/auto-minor-version-upgrade true
                              ::dbi/db-instance-class "db.r4.large"
                              ::r/tags [{::r/key "key"
                                         ::r/value "value"}]}))))))
