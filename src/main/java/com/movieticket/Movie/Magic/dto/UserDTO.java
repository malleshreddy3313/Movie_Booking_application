package com.movieticket.Movie.Magic.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private List<String> roles; // List of role names

    public UserDTO() {
    }


    public UserDTO(Long id, String username, String email, String phoneNumber, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getRoles() {
        return roles;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}