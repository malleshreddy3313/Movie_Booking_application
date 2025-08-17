package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.model.User;
import com.movieticket.Movie.Magic.repository.UserRepository;
import com.movieticket.Movie.Magic.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional // Transactional to ensure lazy loading works if roles are lazy loaded
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // This method allows users to log in with either their username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)
                );

        return CustomUserDetails.create(user);
    }

    // This method is used by JWTAuthenticationFilter to load a user by ID from the JWT token
    @Transactional // Transactional to ensure lazy loading works
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + id)
        );

        return CustomUserDetails.create(user);
    }
}