package com.example.watcher.controller;

import com.example.watcher.model.WatcherModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, WatcherModel> watcherModelKafkaTemplate;
    @GetMapping("/publish/{uniqueDocumentId}")
    public String send(@PathVariable("uniqueDocumentId") final String uniqueDocumentId ) {
        WatcherModel watcherModel = new WatcherModel(uniqueDocumentId,"abc","aa","hh","ggg",9,"hhhh","hhh");

        watcherModelKafkaTemplate.send("request", watcherModel);
      System.out.println("message received " + watcherModel.toString());
        //watcherModelKafkaTemplate.send("watcher",uniqueDocumentId);
        return "success";
    }

    @PostMapping(value = "/postItem",consumes = {"application/json"},produces = {"application/json"})
    public String postJsonMessage(@RequestBody WatcherModel watcherModel){
        watcherModelKafkaTemplate.send("request",watcherModel);
        return "Message published successfully";
    }


}