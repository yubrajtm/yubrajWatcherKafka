package com.example.watcher.controller;

import com.example.watcher.model.WatcherModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MyKafkaListener {
    //listening for topic "request" which receives watcherModel object
    @KafkaListener(topics = "request", groupId = "group_id1", containerFactory = "watcherKafkaListenerContainerFactory")
    public void consumeWatcher(WatcherModel watcherModel){
        System.out.println("Kafka is Listening:"+watcherModel);
    }
}