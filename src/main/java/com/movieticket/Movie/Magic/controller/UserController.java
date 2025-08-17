package com.movieticket.Movie.Magic.controller;

import com.movieticket.Movie.Magic.dto.UserDTO;
import com.movieticket.Movie.Magic.dto.UserUpdateDTO;
import com.movieticket.Movie.Magic.exception.ResourceNotFoundException;
import com.movieticket.Movie.Magic.service.CustomUserDetailsService;
import com.movieticket.Movie.Magic.service.UserService;
import com.movieticket.Movie.Magic.security.CustomUserDetails;
import jakarta.validation.Valid; // Import for @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.movieticket.Movie.Magic.dto.PasswordChangeDTO;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Helper method
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User not authenticated.");
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        throw new ResourceNotFoundException("Could not retrieve user ID from authentication principal. Principal type: " + principal.getClass().getName());
    }

   //retrieve profile
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getMyProfile() {
        Long userId = getCurrentUserId();
        UserDTO userDTO = userService.getUserById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    //update password
    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changeMyPassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        Long userId = getCurrentUserId();
        userService.changeUserPassword(userId, passwordChangeDTO);
        return new ResponseEntity<>("Password changed successfully!", HttpStatus.OK);
    }


    //delete account
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteMyAccount() {
        Long userId = getCurrentUserId();
        userService.deleteUser(userId);
        return new ResponseEntity<>("User account and associated data deleted successfully!", HttpStatus.NO_CONTENT);
    }

}