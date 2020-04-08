package com.example.watcher.controller;

import com.example.watcher.model.WatcherModel;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
@EnableKafka
@Configuration
public class KafkaConfig {

    //configure kafka, and kafkaTemplate for sending topic of type WatcherModel
    @Bean
    public ProducerFactory<String, WatcherModel> watcherProducerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory(config);
    }
    @Bean
    public KafkaTemplate<String, WatcherModel> watcherModelKafkaTemplate(){
        return new KafkaTemplate<String, WatcherModel>(watcherProducerFactory());
    }

    //configure kafka, and kafkaListener to receive topic of type WatcherModel

    @Bean
    public ConsumerFactory<String, WatcherModel> watcherConsumerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id1");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config,
                new StringDeserializer(), new JsonDeserializer<>(WatcherModel.class)); //to deserialize json to WatcherModel class
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WatcherModel> watcherKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, WatcherModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(watcherConsumerFactory());
        return factory;
    }

}
