import mainParser.MainParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import syslogParser.SyslogEntry;
import syslogParser.SyslogParseException;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);
        consumer.subscribe(Arrays.asList("syslog"));

        syslogParser.Parser syslogParser = new syslogParser.Parser();

        MainParser parser = new MainParser(Arrays.asList(new sshdProcessParser.Parser()));

        System.out.print("Consumer is running.");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                try {
                    var json = parser.parse(record.value());
                    System.out.println(json.toJson());
                } catch (SyslogParseException ex) {
                    System.out.printf("The message could not be parsed as syslog (%s)%m", ex.toString());
                }
            }
        }
    }
}
