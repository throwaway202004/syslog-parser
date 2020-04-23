#!/bin/bash

topic="syslog"

while IFS= read -r line
do
 echo "Sending message: $line"
 echo "$line" | kafka_2.12-2.5.0/bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic "$topic" > /dev/null 2>&1
done < /dev/stdin
echo "Done."

