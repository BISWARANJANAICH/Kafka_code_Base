import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.omg.SendingContext.RunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class KafkaConsumerWithThreadsTest {

    public static void main(String[] args) {
            new KafkaConsumerWithThreadsTest().run();
    }

    private KafkaConsumerWithThreadsTest(){}

    private void run(){
        Logger logger = LoggerFactory.getLogger(KafkaConsumerWithThreadsTest.class);
        String topic = "first_topic";
        String groupId = "my-sixth-application";
        String bootstrapServer = "localhost:9092";
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable consumerthread = new Consumerthread(bootstrapServer,
                groupId,
                topic,
                countDownLatch
        );

            Thread thread = new Thread(consumerthread);
            thread.start();

           Runtime.getRuntime().addShutdownHook(new Thread(()->{

               logger.info("Caught ShutDown hook....");
               ((Consumerthread)consumerthread).shutdown();

           }));
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("Application got interupted..");
        }finally {
            logger.info("Application is closing...");
        }
    }

    public static class Consumerthread implements Runnable{


        private Logger logger = LoggerFactory.getLogger(Consumerthread.class);
        private final CountDownLatch latch ;
        KafkaConsumer<String,String> consumer ;

        Consumerthread(String bootstrapServer,
                       String groupId,
                       String topic,
                       CountDownLatch latch){

            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

            this.latch = latch;
            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Arrays.asList(topic));

        }

        @Override
        public void run() {

            try {
                //poll for new data
                while (true) {

                    ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));

                    for (ConsumerRecord<String, String> record : consumerRecords) {

                        logger.info("key:" + record.key() + " | " + "Value : " + record.value() + " | " + "offset : " + record.offset()
                                + " | " + "Partition : " + record.partition());
                    }

                } // while
            }catch (WakeupException wakeupException){
              logger.info("Got signal to shutdown....");
            }finally {
                consumer.close();

                // It will tell our main code that we are done with consumer.
                latch.countDown();
            }

        } // run


      // Its to shutdown the consumer thread
        public void shutdown(){
            //it will interrupt the consumer.poll() and throw WakeUpException
             consumer.wakeup();

        }// shutdown
    }// Consumerthread
} // KafkaConsumerWithThreadsTest
