(ns crucible.aws.ecs.container-definition
  "AWS::ECS::TaskDefinition > ContainerDefinition"
  (:require [clojure.spec.alpha :as s]
            [crucible.aws.ecs.key-value-pair :as key-value-pair]
            [crucible.resources :refer [spec-or-ref]]))

(s/def ::image (spec-or-ref string?))

(s/def ::name (spec-or-ref string?))

(s/def ::command (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::cpu (spec-or-ref integer?))

(s/def ::disable-networking (spec-or-ref boolean?))

(s/def ::dns-search-domains (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::dns-servers (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::docker-labels (s/map-of (s/or :kw keyword? :str string?) (spec-or-ref string?)))

(s/def ::docker-security-options (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::entry-point (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::environment (s/* ::key-value-pair/entity-spec))

(s/def ::essential (spec-or-ref boolean?))

(s/def ::extra-hosts (s/keys :req [::hostname
                                   ::ip-address]
                             :opt []))

(s/def ::links (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::capabilities (s/keys :req []
                              :opt [::add
                                    ::drop]))

(s/def ::host-path (spec-or-ref string?))
(s/def ::container-path (spec-or-ref string?))
(s/def ::permissions (s/coll-of (spec-or-ref string?) :kind vector?))

(s/def ::devices (s/keys :req [::host-path]
                         :opt [::container-path
                               ::permissions]))

(s/def ::init-process-enabled (spec-or-ref boolean?))

(s/def ::linux-parameters (s/keys :req []
                                  :opt [::capabilities
                                        ::devices
                                        ::init-process-enabled]))

(s/def ::log-driver (spec-or-ref string?))
(s/def ::options (s/map-of (s/or :kw keyword? :str string?) (spec-or-ref string?)))

(s/def ::log-configuration (s/keys :req [::log-driver]
                                   :opt [::options]))

(s/def ::memory (spec-or-ref integer?))

(s/def ::memory-reservation (spec-or-ref integer?))

(s/def ::source-volume (spec-or-ref string?))
(s/def ::read-only (spec-or-ref boolean?))

(s/def ::mount-points (s/keys :req [::container-path
                                    ::source-volume]
                              :opt [::read-only]))

(s/def ::container-port (spec-or-ref integer?))
(s/def ::host-port (spec-or-ref integer?))
(s/def ::protocol (spec-or-ref string?))

(s/def ::port-mapping (s/keys :req [::container-port]
                               :opt [::host-port
                                     ::protocol]))

(s/def ::port-mappings (s/* ::port-mapping))

(s/def ::privileged (spec-or-ref boolean?))

(s/def ::readonly-root-filesystem (spec-or-ref boolean?))

(s/def ::hard-limit (spec-or-ref integer?))
(s/def ::soft-limit (spec-or-ref integer?))

(s/def ::ulimits (s/keys :req [::hard-limit
                               ::soft-limit]
                         :opt [::name]))

(s/def ::source-container (spec-or-ref string?))

(s/def ::user (spec-or-ref string?))

(s/def ::volumes-from (s/keys :req [::source-container]
                              :opt [::read-only]))

(s/def ::working-directory (spec-or-ref string?))

(s/def ::container-definition (s/keys :req [::image
                                            ::name]
                                      :opt [::command
                                            ::cpu
                                            ::disable-networking
                                            ::dns-search-domains
                                            ::dns-servers
                                            ::docker-labels
                                            ::docker-security-options
                                            ::entry-point
                                            ::environment
                                            ::essential
                                            ::extra-hosts
                                            ::enable-dns-hostnames
                                            ::links
                                            ::linux-parameters
                                            ::log-configuration
                                            ::memory
                                            ::memory-reservation
                                            ::mount-points
                                            ::port-mappings
                                            ::privileged
                                            ::readonly-root-filesystem
                                            ::ulimits
                                            ::user
                                            ::volumes-from
                                            ::working-directory]))
