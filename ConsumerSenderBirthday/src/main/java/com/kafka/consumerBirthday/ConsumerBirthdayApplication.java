package com.kafka.consumerBirthday;

import com.ex.Example;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class ConsumerBirthdayApplication implements CommandLineRunner {

    KafkaConsumer<String, String> consumer;
    ObjectMapper objectMapper = new ObjectMapper();


    public static void main(String[] args) {
        SpringApplication.run(ConsumerBirthdayApplication.class, args);
    }

    private void init() {

        String topicName = "topicClean";

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "groupBirthday");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topicName));

        //print the topic name
        System.out.println("Consumer Birthday - Subscribed to topic " + topicName);
    }


    @Override
    public void run(String... args) throws Exception {
        init();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                Example exuser = objectMapper.readValue(record.value(), Example.class);


                DateTimeFormatter DATE_FORMAT =
                        new DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy[ [HH][:mm][:ss][.SSS]]")
                                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                                .toFormatter();

                LocalDate dateBirthday = LocalDate.parse(exuser.getBirthday().getDmy(), DATE_FORMAT);

                int day = LocalDate.now().getDayOfMonth();
                int month = LocalDate.now().getMonthValue();

                int dayBirthday = dateBirthday.getDayOfMonth();
                int monthBirthday = dateBirthday.getMonthValue();

                if (day == dayBirthday
                        && month == monthBirthday) {
                    System.out.println("Birthday " + exuser.getName() + " " + exuser.getBirthday().getDmy());
                } else {
                    System.out.println("Not Birthday for  " + exuser.getName() + " " + exuser.getBirthday().getDmy());
                }
            }
        }
    }
}
