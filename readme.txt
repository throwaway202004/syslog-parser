TODO
[] Check that execute flag is still set when cloning
[] Move java app to a consumer map


Instructions
============

These instructions are for Linux.

Run the script: ./setup.sh

This script will look for a kafka tarball or if its not found download the latest version from  
http://apache.mirrors.spacedump.net/kafka/2.5.0/kafka_2.12-2.5.0.tgz

It will then procede with extracting the downloaded file. It will then start the required services and create the topic ("syslog").

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


(Start consumer - DEBUGING)
---------------------------
kafka_2.12-2.5.0/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic syslog


Send syslog messages
--------------------
cat syslog.txt | ./producer.sh

