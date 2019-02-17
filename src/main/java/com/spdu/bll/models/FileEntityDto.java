package com.spdu.bll.models;

import com.spdu.domain_models.entities.FileEntity;

public class FileEntityDto {
    private long id;
    private String name;
    private String path;
    private String contentType;
    private long ownerId;

    public FileEntityDto(FileEntity fileEntity) {
        this.id = fileEntity.getId();
        this.name = fileEntity.getName();
        this.path = fileEntity.getPath();
        this.contentType = fileEntity.getContentType();
        this.ownerId = fileEntity.getOwnerId();
    }

    public FileEntityDto() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
