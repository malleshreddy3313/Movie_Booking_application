package com.movieticket.Movie.Magic.security;

import com.movieticket.Movie.Magic.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        logger.info("Entering shouldNotFilter for path: {} and method: {}", path, method);
        if (pathMatcher.match("/api/auth/**", path)) {
            logger.info("Path {} matched /api/auth/**. Skipping filter.", path);
            return true;
        }

        if (method.equals("GET")) {
            if (pathMatcher.match("/api/movies/**", path) ||
                    pathMatcher.match("/api/theaters/**", path) ||
                    pathMatcher.match("/api/showtimes/**", path)) {
                logger.info("Public GET path {} matched. Skipping filter.", path);
                return true;
            }
        }

        logger.info("Path {} requires filtering. Proceeding with doFilterInternal.", path);
        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.info("Entering doFilterInternal for path: {}", request.getRequestURI());

        try {
            String jwt = getJwtFromRequest(request);
            logger.info("Extracted JWT: {}", StringUtils.hasText(jwt) ? "Present" : "Not Present");

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                logger.info("JWT is present and valid."); // LOG
                String username = jwtTokenProvider.getUsernameFromJwt(jwt);
                logger.info("Username from JWT: {}", username); // LOG username

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                logger.info("Loaded UserDetails for: {} with authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());


                if (SecurityContextHolder.getContext().getAuthentication() == null ||
                        !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Authentication set in SecurityContextHolder."); // LOG
                } else {
                    logger.info("Authentication already present in SecurityContextHolder for path {}.", request.getRequestURI());
                }

            } else if (StringUtils.hasText(jwt) && !jwtTokenProvider.validateToken(jwt)) {
                logger.warn("JWT is present but invalid or expired for path: {}", request.getRequestURI());
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context for path {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}