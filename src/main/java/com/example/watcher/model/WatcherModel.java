package com.example.watcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class WatcherModel {
    private String uniqueDocumentId = UUID.randomUUID().toString();
    private String eventStatus;
    private String PathValue;
    private String fileName;

    public String getFileType() {
        return fileType;
    }

    public WatcherModel(){}

    public WatcherModel(String uniqueDocumentId, String eventStatus, String pathValue, String fileName, String fileType, double size, String modifiedDate, String createdDate) {
        this.uniqueDocumentId = uniqueDocumentId;
        this.eventStatus = eventStatus;
        PathValue = pathValue;
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    private String fileType;
    private double size;
    private String modifiedDate;
    private String createdDate;

    public String getUniqueDocumentId() {
        return uniqueDocumentId;
    }

    public void setUniqueDocumentId(String uniqueDocumentId) {
        this.uniqueDocumentId = uniqueDocumentId;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getPathValue() {
        return PathValue;
    }

    public void setPathValue(String pathValue) {
        PathValue = pathValue;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "WatcherModel{" +
                "uniqueDocumentId='" + uniqueDocumentId + '\'' +
                ", eventStatus='" + eventStatus + '\'' +
                ", PathValue='" + PathValue + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", size=" + size +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
