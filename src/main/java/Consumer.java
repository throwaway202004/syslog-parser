import mainParser.MainParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import syslogParser.SyslogParseException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * This class implements the actual Kafka consumer.
 * It sets up the consumer to listen to the "syslog" topic and
 * then goes into a loop polling for new records every 100 milliseconds.
 * If some records exist they are parsed and the resulting JSON object is
 * written to standard out. If a record could not be parsed, the error is
 * output and the loop continues.
 *
 */
public class Consumer {
    private static final String TOPIC = "syslog";
    private static final String BOOTSTRAP_SERVER = "localhost:9092";
    public static final String GROUP_ID = "test";
    public static final String AUTO_COMMIT = "true";
    public static final String AUTO_COMMIT_INTERVAL = "1000";
    public static final String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    public static final String VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";

    /**
     * This is the main program and it should be executed from the command line.
     *
     * This program could of course be further enhanced by taking in an argument for
     * which topic to listen to and the Kafka settings could be read from a file.
     *
     */
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", BOOTSTRAP_SERVER);
        props.setProperty("group.id", GROUP_ID);
        props.setProperty("enable.auto.commit", AUTO_COMMIT);
        props.setProperty("auto.commit.interval.ms", AUTO_COMMIT_INTERVAL);
        props.setProperty("key.deserializer", KEY_DESERIALIZER);
        props.setProperty("value.deserializer", VALUE_DESERIALIZER);

        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);
        consumer.subscribe(Arrays.asList(TOPIC));

        var ipAddress = getIpAddress();

        MainParser parser = new MainParser(Arrays.asList(new sshdProcessParser.Parser()), ipAddress);

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

    /**
     * This methods tries to find the IP address of the process.
     * By using a datagram socket (that doesn't need to be connected) the
     * preferred IP address out of many network interfaces can be found.
     * @return String
     */
    private static String getIpAddress() {
        var host = "8.8.8.8";
        var port = 10000;
        var ip = "127.0.0.1";

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName(host), port);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            //
            // Should the app exit if the IP address could not be determined or is it OK to
            // start running?
            // Let's warn the user and return the loopback address.
            //
            System.out.println("Warning: IP address of consumer could not be determined.");
        }
        return ip;
    }
}
