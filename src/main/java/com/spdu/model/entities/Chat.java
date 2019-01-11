package com.spdu.model.entities;

import com.spdu.model.constants.ChatType;

import java.time.LocalDateTime;

public class Chat {
    private long id;
    private String name;
    private LocalDateTime dateOfCreated;
    private ChatType chatType;
    private String tags;
    private String description;
}
