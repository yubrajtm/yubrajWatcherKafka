package com.example.watcher.controller;

import com.example.watcher.model.WatcherModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("watch")
public class FirstController {


    @Autowired
    private KafkaTemplate<String, WatcherModel> watcherModelKafkaTemplate;


    Path directoryPath = null;

    @GetMapping("test")
    public HashMap<String, String> test() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", "bb");
        return map;
    }

    @GetMapping("/get")
    public WatcherModel getData(@RequestParam(name = "dir") String dir) {
        directoryPath = FileSystems.getDefault().getPath(dir);
        System.out.println(directoryPath);
        WatcherModel watcherModel = new WatcherModel();
        try {
            WatchService watchService = directoryPath.getFileSystem().newWatchService();
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            //Start infinite loop to watch changes on the directory
            while (true) {

                WatchKey watchKey = watchService.take();

                // poll for file system events on the WatchKey
                for (final WatchEvent<?> event : watchKey.pollEvents()) {
                    //Calling method
                    watcherModel = takeActionOnChangeEvent(event);
                }
                if(watcherModel != null){
                    break;
                }

                //Break out of the loop if watch directory got deleted
                if (!watchKey.reset()) {
                    watchKey.cancel();
                    watchService.close();
                    System.out.println("Watch directory got deleted. Stop watching it.");
                    //Break out from the loop
                    break;
                }
            }

        } catch (InterruptedException interruptedException) {
            System.out.println("Thread got interrupted:"+interruptedException);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        System.out.println("Watch Model ==="+ watcherModel.toString());
        return watcherModel;

    }

    private WatcherModel takeActionOnChangeEvent(WatchEvent<?> event) throws IOException {

        WatchEvent.Kind<?> kind = event.kind();
        Path entryCreated = (Path) event.context();
        Path child = directoryPath.resolve(entryCreated);
        WatcherModel watcherModel = new WatcherModel();
        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            BasicFileAttributes attr = Files.readAttributes(child, BasicFileAttributes.class);
            FileTime lastModifiedTime = Files.getLastModifiedTime(child, LinkOption.NOFOLLOW_LINKS);
            String lastModifiedTimeAsString = format(lastModifiedTime.toMillis());

            watcherModel.setModifiedDate(lastModifiedTimeAsString);
            watcherModel.setFileName(entryCreated.toString());
            watcherModel.setPathValue(directoryPath.toString());

            FileTime date = attr.creationTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateCreated = df.format(date.toMillis());
            watcherModel.setCreatedDate(dateCreated);

            Optional<String> typeOfFile= getExtensionByStringHandling(entryCreated.toString());
            if(typeOfFile.isPresent()) {
                watcherModel.setFileType(typeOfFile.get());
            }else {
                watcherModel.setFileType(null);
            }

            watcherModel.setEventStatus(event.kind().name());
            watcherModel.setSize(attr.size());
        }
        else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
            BasicFileAttributes attr = Files.readAttributes(child, BasicFileAttributes.class);
            FileTime lastModifiedTime = Files.getLastModifiedTime(child, LinkOption.NOFOLLOW_LINKS);
            String lastModifiedTimeAsString = format(lastModifiedTime.toMillis());
            Path entryModified = (Path) event.context();
            watcherModel.setModifiedDate(lastModifiedTimeAsString);
            watcherModel.setFileName(entryModified.toString());
            watcherModel.setPathValue(directoryPath.toString());

            FileTime date = attr.creationTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateCreated = df.format(date.toMillis());
            watcherModel.setCreatedDate(dateCreated);


            Optional<String> typeOfFile= getExtensionByStringHandling(entryCreated.toString());
            if(typeOfFile.isPresent()) {
                watcherModel.setFileType(typeOfFile.get());
            }else {
                watcherModel.setFileType(null);
            }


            watcherModel.setEventStatus(event.kind().name());
            watcherModel.setSize(attr.size());
        }
        else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
            watcherModel.setEventStatus(event.kind().name());
            watcherModel.setFileName(entryCreated.toString());

            //BasicFileAttributes attr = Files.readAttributes(directoryPath, BasicFileAttributes.class);
            FileTime lastModifiedTime = Files.getLastModifiedTime(directoryPath, LinkOption.NOFOLLOW_LINKS);
            String lastModifiedTimeAsString = format(lastModifiedTime.toMillis());
            watcherModel.setModifiedDate(lastModifiedTimeAsString);
            watcherModel.setPathValue(directoryPath.toString());


        }
        watcherModelKafkaTemplate.send("request",watcherModel);
        System.out.println( "Message published successfully");

        return watcherModel;
    }

    public static String format(long time) {
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date(time));
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    }



