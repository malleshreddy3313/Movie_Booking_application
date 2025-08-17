package com.movieticket.Movie.Magic.dto;
import jakarta.validation.constraints.NotBlank;
public class LoginRequest {
    @NotBlank(message = "Username or email cannot be empty")
    private String usernameOrEmail; // Can be either username or email for login

    @NotBlank(message = "Password cannot be empty")
    private String password;


    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
