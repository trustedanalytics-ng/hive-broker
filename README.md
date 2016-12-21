hive-broker
===========

Broker for creation databases in Hive.

# How to use it?
To use hive-broker, you need to build it from sources configure, deploy, create instance and bind it to your app. Follow steps described below. 

## Build 
Run command for compile and package.: 
```
mvn clean package
```
Run optional command for create docker image:
```
mvn docker:build
```

## Plans

  * Standard : Create private Hive database within storage space shared across your organization

## Configure

###Profiles 
Each profile describes authentication used during communication with Hadoop, at least one is required: 

  * simple
  * kerberos
  
### Broker library
Broker library is java spring library, which simplifies broker store implementation. Currently hive-broker uses zookeeper-broker store implementation, which stores information about every instance in secured znode.

* obligatory
  * simple
    * STORE_CLUSTER : zookeeper quorum address (example: host1:2181,host2:2181,host:2181)
    * STORE_USER : user used to authenticate with zookeeper broker store
    * STORE_PASSWORD : password for store user
  * kerberos
    * STORE_KEYTABPATH : path to the keytab file, which will be used to authenticate store user in kerberos

### Other
* obligatory
  * simple
    * USER_PASSWORD - password to interact with service broker Rest API
    * BASE_GUID - base id for catalog plan creation
    * CATALOG_SERVICENAME - service name in catalog (default: hive)
    * CATALOG_SERVICEID - service id in catalog (default: hive)
    * SYSTEM_USER : name of the regular user which will be used to create znodes
    * HIVE_SERVER2_THRIFT_BIND_HOST - hive server2 host address
    * HIVE_SERVER2_THRIFT_PORT - hive server2 port
    * HIVE_CONFIGURATION_PATH : path of the hive-conf directory
    * HIVE_SUPERUSER - superuser principal name (user with full permissions on hive server) 
  * kerberos
    * KRB_REALM : Kerberos Realm (kerberos profile required)
    * KRB_KDC : Key Distribution Center Adddress (kerberos profile required)
    * HIVE_SUPERUSER_KEYTAB_PATH - path to the keytab file, which will be used to authenticate HIVE_SUPERUSER in kerberos
    * SYSTEM_USER_KEYTAB_PATH : path to the keytab file, which will be used to authenticate SYSTEM_USER in kerberos


## Useful links

Offering template for TAP platform:
 * https://github.com/intel-data/tap-deploy/blob/master/roles/tap-marketplace-offerings/templates/hive/offering.json

Broker library:
 * https://github.com/intel-data/broker-lib    

Cloud foundry resources that are helpful when troubleshooting service brokers : 
 * http://docs.cloudfoundry.org/services/api.html
 * http://docs.cloudfoundry.org/devguide/services/managing-services.html#update_service
 * http://docs.cloudfoundry.org/services/access-control.html

## On the app side

For spring applications use https://github.com/trustedanalytics/hadoop-spring-utils. 

For regular java applications use https://github.com/trustedanalytics/hadoop-utils. 