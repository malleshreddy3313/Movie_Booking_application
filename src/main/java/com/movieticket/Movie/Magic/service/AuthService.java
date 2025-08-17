package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.dto.LoginRequest;
import com.movieticket.Movie.Magic.dto.UserRegistrationDTO;
import com.movieticket.Movie.Magic.dto.AuthResponseDTO;
import com.movieticket.Movie.Magic.exception.BadRequestException;
import com.movieticket.Movie.Magic.exception.ResourceNotFoundException;
import com.movieticket.Movie.Magic.model.User;
import com.movieticket.Movie.Magic.model.Role;
import com.movieticket.Movie.Magic.model.RoleName;
import com.movieticket.Movie.Magic.repository.UserRepository;
import com.movieticket.Movie.Magic.repository.RoleRepository;
import com.movieticket.Movie.Magic.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new BadRequestException("Username '" + registrationDTO.getUsername() + "' is already taken!");
        }

        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new BadRequestException("Email '" + registrationDTO.getEmail() + "' is already registered!");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setPhoneNumber(registrationDTO.getPhoneNumber());

        final Set<Role> assignedRoles;

        String roleFromDto = registrationDTO.getRole();

        if (roleFromDto != null && !roleFromDto.isEmpty()) {
            try {
                String tempNormalizedRoleString = roleFromDto.toUpperCase();
                if (tempNormalizedRoleString.startsWith("ROLE_")) {
                    tempNormalizedRoleString = tempNormalizedRoleString.substring(5);
                }
                // Declare a new final variable for use in lambda
                final String finalNormalizedRoleString = tempNormalizedRoleString;

                RoleName roleEnum = RoleName.valueOf(finalNormalizedRoleString);

                Role foundRole = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new ResourceNotFoundException("Role '" + finalNormalizedRoleString + "' not found in the database.")); // Use the final variable

                assignedRoles = Collections.singleton(foundRole);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role specified: '" + roleFromDto + "'. Role must be 'USER' or 'ADMIN'.");
            }
        } else {
            Role userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Default 'USER' role not found. Please ensure it exists in the database."));
            assignedRoles = Collections.singleton(userRole);
        }

        user.setRoles(assignedRoles);

        return userRepository.save(user);
    }

    public AuthResponseDTO authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            return new AuthResponseDTO(token);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username/email or password!", e);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }
}