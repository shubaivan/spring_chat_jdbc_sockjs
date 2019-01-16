package com.spdu.model.entities;

import com.spdu.model.constants.ChatType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;
//
//    private LocalDateTime dateOfCreated;
//    private ChatType chatType;
//    private String tags;
//    private String description;

    public Chat() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
