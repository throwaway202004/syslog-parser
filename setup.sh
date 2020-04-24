#!/bin/bash

file=`find . -name "kafka*.tgz" -type f`

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

echo "Starting Zookeeper"
kafka_2.12-2.5.0/bin/zookeeper-server-start.sh kafka_2.12-2.5.0/config/zookeeper.properties > /dev/null 2>&1 &

echo
echo "Starting server"
kafka_2.12-2.5.0/bin/kafka-server-start.sh kafka_2.12-2.5.0/config/server.properties > /dev/null 2>&1 &

echo
echo "Creating topic '$topic'"
kafka_2.12-2.5.0/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic syslog

echo
echo "Topic created"
echo
echo

#echo "Building Java application"
#mvn package
#

# Start the consumer java app
# Let is print to stdout and also pipe stdout to a file
#
#echo "$consumer_cmd"
#
# Print instructions of how to run the producer.sh script in another windowd
#

