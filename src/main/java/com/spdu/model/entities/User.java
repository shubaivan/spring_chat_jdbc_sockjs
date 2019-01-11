package com.spdu.model.entities;

import com.spdu.model.constants.UserRole;

import java.time.LocalDate;

public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private LocalDate dateOfRegistration;
    private long avatar;
    private String urlLinkedin;
    private String urlFacebook;
    private String urlGit;
    private LocalDate dateOfBirth;
    private UserRole userRole;

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
