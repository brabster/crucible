(ns crucible.aws.ecs.task-test
  (:require [crucible.aws.ecs.task-definition :as task]
            [crucible.aws.ecs.container-definition :as container]
            [crucible.aws.ecs.secret :as secret]
            [crucible.core :refer [xref]]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]))

(deftest task-tests

  (testing "task with secrets"
    (is
      (s/valid? ::task/task-definition
                {::task/cpu "2048"
                 ::task/memory "4096"
                 ::task/container-definitions [{::container/name "rclone"
                                                ::container/image "rclone/rclone"
                                                ::container/secrets [{::secret/name "rclone-access-key"
                                                                      ::secret/value-from "arn:aws:secretsmanager:region:aws_account_id:secret:value-u9bH6K"}]}]}))))
