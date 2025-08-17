package com.movieticket.Movie.Magic.controller;

import com.movieticket.Movie.Magic.dto.LoginRequest;
import com.movieticket.Movie.Magic.dto.AuthResponseDTO;
import com.movieticket.Movie.Magic.dto.UserRegistrationDTO;
import com.movieticket.Movie.Magic.model.User;
import com.movieticket.Movie.Magic.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        // Calls the registerUser method in AuthService
        authService.registerUser(registrationDTO);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody LoginRequest loginRequest) {
        AuthResponseDTO authResponse = authService.authenticateUser(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


}