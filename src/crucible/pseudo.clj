(ns crucible.pseudo
  "Psuedo Parameters http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/pseudo-parameter-reference.html")

(defn- ps [type]
  [:pseudo type])

(def account-id
  (ps :account-id))

(def region
  (ps :region))

(def notification-arns
  (ps :notification-arns))

(def no-value
  (ps :no-value))

(def stack-id
  (ps :stack-id))

(def stack-name
  (ps :stack-name))



