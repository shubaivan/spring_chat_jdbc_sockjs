package com.spdu.model.entities;

import java.time.LocalDate;

public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private LocalDate dateOfRegistration;

    public User() {

    }

    public User(long id,
                String firstName, String lastName,
                String email, String userName,
                String password, LocalDate dateOfRegistration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.dateOfRegistration = dateOfRegistration;
    }
}
