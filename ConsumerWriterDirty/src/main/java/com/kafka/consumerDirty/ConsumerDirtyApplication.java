package com.kafka.consumerDirty;

import com.ex.Example;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class ConsumerDirtyApplication implements CommandLineRunner {

    KafkaConsumer<String, String> consumer;
    ObjectMapper objectMapper = new ObjectMapper();
    String topicCleanName = "topicClean";

    @Autowired
    UserJdbcRepository repository;
    Producer<String, String> producer;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerDirtyApplication.class, args);
    }

    private void initConsumer() {

        String topicName = "topicDirty";

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "groupDirty");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topicName));

        //print the topic name
        System.out.println("Consumer Dirty - Subscribed to topic " + topicName);
    }

    private void initProducer() {


        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);

    }

    @Override
    public void run(String... args) throws Exception {
        initConsumer();
        initProducer();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                // 	print the offset,key and value for the consumer records.
                Example exuser = objectMapper.readValue(record.value(), Example.class);
                this.repository.insert(new User(exuser.getName(), record.value()));
                System.out.printf("Received in TopicDirty offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());

                exuser.setCreditCard(null);

                System.out.println("User send to topic " + topicCleanName + " " + objectMapper.writeValueAsString(exuser));

                producer.send(new ProducerRecord<String, String>(topicCleanName, objectMapper.writeValueAsString(exuser)));
            }
        }
    }
}
