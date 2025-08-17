package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.dto.UserDTO;
import com.movieticket.Movie.Magic.dto.UserUpdateDTO;
import com.movieticket.Movie.Magic.dto.PasswordChangeDTO;
import com.movieticket.Movie.Magic.exception.BadRequestException;
import com.movieticket.Movie.Magic.exception.ResourceNotFoundException;
import com.movieticket.Movie.Magic.model.User;
import com.movieticket.Movie.Magic.model.Role;
import com.movieticket.Movie.Magic.model.RoleName;
import com.movieticket.Movie.Magic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.movieticket.Movie.Magic.repository.BookingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                roleNames
        );
    }


    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return convertToUserDTO(user);
    }


    @Transactional
    public UserDTO updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));


        if (!existingUser.getUsername().equals(userUpdateDTO.getUsername())) {
            userRepository.findByUsername(userUpdateDTO.getUsername()).ifPresent(user -> {
                throw new BadRequestException("Username '" + userUpdateDTO.getUsername() + "' is already taken.");
            });
        }


        if (!existingUser.getEmail().equals(userUpdateDTO.getEmail())) {
            userRepository.findByEmail(userUpdateDTO.getEmail()).ifPresent(user -> {
                throw new BadRequestException("Email '" + userUpdateDTO.getEmail() + "' is already registered.");
            });
        }

        // Update allowed fields
        existingUser.setUsername(userUpdateDTO.getUsername());
        existingUser.setEmail(userUpdateDTO.getEmail());
        existingUser.setPhoneNumber(userUpdateDTO.getPhoneNumber());

        User updatedUser = userRepository.save(existingUser);
        return convertToUserDTO(updatedUser);
    }

    @Transactional
    public void changeUserPassword(Long userId, PasswordChangeDTO passwordChangeDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Verify current password
        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match.");
        }

        // Optional: Prevent changing to the same password (can be a business rule)
        if (passwordEncoder.matches(passwordChangeDTO.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the old password.");
        }

        // Encode and set new password
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
    }


    @Transactional
    public void deleteUser(Long userId) {
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));


        List<com.movieticket.Movie.Magic.model.Booking> userBookings = bookingRepository.findByUser(userToDelete);

        bookingRepository.deleteAll(userBookings); // This will delete associated booked seats due to cascade

        userRepository.delete(userToDelete);
    }
}