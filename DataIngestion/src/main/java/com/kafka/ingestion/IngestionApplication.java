package com.kafka.ingestion;

import com.ex.Example;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@EnableScheduling
@SpringBootApplication
public class IngestionApplication implements CommandLineRunner {

    RestTemplate restTemplate;
    String fooResourceUrl = "https://uinames.com/api/?amount=500&ext";
    String topicName = "topicDirty";
    Producer<String, String> producer;
    ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(IngestionApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        init();
    }

    private void init() {

        //Assign topicName to string variable
        // String topicName = args[0].toString();
        topicName = "topicDirty";

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 1);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("transactional.id", "" + System.currentTimeMillis());

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        restTemplate = new RestTemplate();


        producer = new KafkaProducer<String, String>(props);

    }


    @Scheduled(fixedRate = 20000, initialDelay = 1000)
    public void callService() throws Exception {
        init();
        producer.initTransactions();
        System.out.println("Check data from service .... ");


        try {

            ResponseEntity<List<Example>> response = restTemplate.exchange(
                    fooResourceUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Example>>() {
                    });
            producer.beginTransaction();
            for (Iterator iterator = response.getBody().iterator(); iterator.hasNext(); ) {
                Example type = (Example) iterator.next();
                System.out.println("User send to topic " + topicName + " UserName: " + type.getName() + " " + objectMapper.writeValueAsString(type));

                producer.send(new ProducerRecord<String, String>(topicName, type.getRegion(), objectMapper.writeValueAsString(type)));


            }
            producer.commitTransaction();


        } catch (Exception e) {
            producer.abortTransaction();
            System.out.println("ERROR" + e.toString());
        } finally {
            producer.close();
        }


    }
}
