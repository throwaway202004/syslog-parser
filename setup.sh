#!/bin/bash

topic="syslog"

file=`find . -maxdepth 1 -type f -name "kafka*.tgz"`

if [ "$file" ]
then
 echo "Found $file"
 version=`echo $file | sed 's/.*kafka_\(.*\).tgz/\1/'`
else
 echo "Downloading..."
 wget http://apache.mirrors.spacedump.net/kafka/2.5.0/kafka_2.12-2.5.0.tgz
 version="2.12-2.5.0"
fi

tar -xzf kafka_"$version".tgz

kafka_dir="kafka_$version"

echo "Starting Zookeeper"
"$kafka_dir"/bin/zookeeper-server-start.sh "$kafka_dir"/config/zookeeper.properties > /dev/null 2>&1 &

echo
echo "Starting server"
"$kafka_dir"/bin/kafka-server-start.sh "$kafka_dir"/config/server.properties > /dev/null 2>&1 &

echo
echo "Wait some time for broker to start"
echo

sleep 3

echo
echo "Creating topic '$topic'"
"$kafka_dir"/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic syslog

echo
echo "Topic created"
echo
echo

echo "Building Java application"
mvn clean
mvn package

echo
echo "Set up finished"
echo
echo "Now, start the consumer process with the command"
echo
echo "java -jar target/syslog-parser-1.0-SNAPSHOT-jar-with-dependencies.jar"
echo
echo
echo "Open up another terminal window and run the producer process:"
echo
echo "cat syslog.txt | ./producer.sh"

