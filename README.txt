https://github.com/throwaway202004/syslog-parser

Instructions
============

These instructions are for Linux.

Run the script: ./setup.sh

This script will look for a kafka tarball or if its not found download the latest version from  
http://apache.mirrors.spacedump.net/kafka/2.5.0/kafka_2.12-2.5.0.tgz

It will then proceed with extracting the downloaded file. When Kafka is extracted, the script will continue
with starting the required services and create the a "syslog" topic.


Manual install instructions
===========================
In case the setup.sh script does not work (it is not extensively tested on different OSes/machine etc) the
following steps can be performed.

(All the below steps are expected to be run in the root folder of the git repo.)

Get Kafka and install it
------------------------
wget http://apache.mirrors.spacedump.net/kafka/2.5.0/kafka_2.12-2.5.0.tgz
tar -xzf kafka_2.12-2.5.0.tgz

Start Kafka Zookeeper
---------------------
kafka_2.12-2.5.0/bin/zookeeper-server-start.sh kafka_2.12-2.5.0/config/zookeeper.properties 

Start Kafka server
------------------
kafka_2.12-2.5.0/bin/kafka-server-start.sh kafka_2.12-2.5.0/config/server.properties 

Create the topic
----------------
kafka_2.12-2.5.0/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic syslog

Compile, test and package the consumer
--------------------------------------
mvn clean
mvn package

Run the consumer
----------------
java -jar target/syslog-parser-1.0-SNAPSHOT-jar-with-dependencies.jar

Send syslog messages
--------------------
cat syslog.txt | ./producer.sh

