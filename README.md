# infrastructure-bootstrapping

work in progress


##Basics

#### building blocks
- artifacts: creates an artifact - e.g. from git to .jar
- configuration: defines a host to run something on - e.g. install docker engine on host
- authentication: define public keys for ssh and attach to configurations
- users: define users that should be created on machines

#### composites
- deployment: takes an artifact and produces a deployable artifact - e.g. docker image with ENVs, service name/ip, ports
- machine: takes a configuration and provisions a machine with it, either physical or virtual machine
- cloud: a composition of machines. defines a platform - e.g. pure docker or kubernetes

##Example usage
- define cloud of (new and) existing machines -> normalize all machines, e.g. disable root and set public key auth + set authorized keys
- define cloud of virtual machines -> provision vms with a provider, e.g. xen server
- define cloud and deployment -> deploy deployment onto cloud
- update cloud and deployment -> upgrade old to new deployment

## Plugins
- comes default with
    - cloud provision with xen server and centos
    - normalizing of machines through ssh
    - configuration for docker hosts
    - build and deploy with jenkins dsl plugin
        - building of artifacts of type docker, maven and gradle
        - packaging artifacts as docker images
        - deploying to docker hosts
        
Plugins can be exchanges for other providers - e.g. instead of docker hosts one could configure machines for and deploy to kubernetes

##Goal
- create configurations through web frontend
- trigger actions by posting configuration to app
- bootstrap infrastructure for the needs of your organisation