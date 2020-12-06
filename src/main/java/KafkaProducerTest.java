import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.util.Properties;

public class KafkaProducerTest {
    public static Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);
    public static void main(String[] args) {

        ProducerRecord<String,String> producerRecord = null;
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int i =0 ;i < 20 ; i++) {

            producerRecord=
                    new ProducerRecord<String, String>("first_topic","Its the first Message published :), No: " + Integer.toString(i));

            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        logger.info("topic : " + recordMetadata.topic() + " | " + "Partition : " + recordMetadata.partition()
                                + "| " + "offset : " + recordMetadata.offset() + " | " + "TimeStamp : " + recordMetadata.timestamp());

                    } else {

                        logger.error("Error While Publishing", e);
                    }
                }
            });
        }
        producer.flush();
        producer.close();

    }

}
