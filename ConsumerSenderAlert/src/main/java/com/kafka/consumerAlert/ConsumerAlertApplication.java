package com.kafka.consumerAlert;

import com.ex.Example;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class ConsumerAlertApplication implements CommandLineRunner {

    KafkaConsumer<String, String> consumer;
    ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(ConsumerAlertApplication.class, args);
    }

    private void init() {

        String topicName = "topicDirty";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "groupSenderMail");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topicName));

        //print the topic name
        System.out.println("Consumer Alert - Subscribed to topic " + topicName);
    }


    @Override
    public void run(String... args) throws Exception {
        init();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                Example exuser = objectMapper.readValue(record.value(), Example.class);


                if (exuser.getRegion().equalsIgnoreCase("China")) {
                    String email = exuser.getEmail();
                    System.out.println("Sending mail to " + email);
                } else {
                    System.out.println("Not send mail for " + exuser.getRegion());
                }
            }
        }
    }

}
