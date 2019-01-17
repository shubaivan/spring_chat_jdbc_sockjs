package com.spdu.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "userId")
    private long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "contentType")
    private String contentType;

    public FileEntity() {
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
}