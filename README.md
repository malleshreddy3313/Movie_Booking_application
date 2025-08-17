
Movie Magic Backend
A robust and secure backend system for a movie ticket booking application, built with Spring Boot. This project provides all the necessary APIs for managing movies, theaters, showtimes, users, and bookings, including a secure authentication and authorization system using JSON Web Tokens (JWT).

🚀 Features
User Management: Secure registration, login, and user profile management.
Authentication & Authorization: JWT-based authentication for secure access to protected resources.
Movie Management: CRUD operations for movies, including details like title, genre, duration, and release date.
Theater & Showtime Management: APIs to manage theaters and their associated showtimes.
Booking System: A robust system for booking movie tickets.
Error Handling: Custom exception handling for a more controlled API response.

🛠️ Tech Stack
Backend Framework: Spring Boot
Database: PostgreSQL
Security: Spring Security
Authentication: JWT (JSON Web Tokens)
Build Tool: Maven
API Documentation: (If you used Swagger/OpenAPI, mention it here)

📁 Project Structure
Movie-Magic/
├── .idea/
├── .mvn/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/movieticket/MovieMagic/
│ │ │ ├── advice/ # Global exception handling
│ │ │ ├── config/ # Security and other configurations
│ │ │ ├── controller/ # REST API endpoints
│ │ │ ├── dto/ # Data Transfer Objects
│ │ │ ├── exception/ # Custom exception classes
│ │ │ ├── model/ # JPA Entities for the database
│ │ │ ├── repository/ # Data access layer (Spring Data JPA)
│ │ │ ├── security/ # JWT and security-related classes
│ │ │ └── service/ # Business logic layer
│ │ │ └── MovieMagicApplication.java # Main application entry point
│ │ └── resources/
│ │ ├── static/ # Static web resources
│ │ ├── templates/ # (If using Thymeleaf)
│ │ └── application.properties # Application configuration
│ └── test/
│ └── java/
│ └── com/movieticket/MovieMagic/
│ └── MovieMagicApplicationTests.java # Unit/Integration tests
├── target/
├── .gitignore
├── HELP.md
└── pom.xml # Maven project configuration
🔌 API Endpoints
All endpoints are relative to https://localhost:8080.

Authentication & User
POST /api/auth/register

Description: Registers a new user with the provided credentials.

Request Body: { "username": "...", "password": "...", "email": "..." }

POST /api/auth/login
Description: Authenticates a user and returns a JWT token for subsequent requests.
Request Body: { "username": "...", "password": "..." }

Movies
  GET /api/movies
  Description: Retrieves a list of all movies.
  Authorization: Not required.

  POST /api/movies
  Description: Creates a new movie.
  Authorization: JWT token required.

Theatres
  GET /api/theatres
  Description: Retrieves a list of all theaters.
  Authorization: Not required.

  POST /api/theatres
  Description: Creates a new theater.
  Authorization: JWT token required.

Showtimes
  GET /api/showtimes
  Description: Retrieves a list of all showtimes.
  Authorization: Not required.

  POST /api/showtimes
  Description: Creates a new showtime for a movie in a theater.
  Authorization: JWT token required.

Bookings
  POST /api/booking
  Description: Creates a new booking for a specific showtime.
  Authorization: JWT token required.
  Request Body: { "showtimeId": "...", "seats": [...] }
  
  GET /api/my-bookings
  Description: Retrieves all bookings made by the currently authenticated user.
  Authorization: JWT token required.

  GET /bookings/by-showtime/{showtimeId}
  Description: Retrieves all bookings for a specific showtime. This is useful for checking seat availability.
  Path Parameter: showtimeId
  Authorization: JWT token required.

  POST /api/bookings/{id}/cancel
  Description: Cancels a booking by its ID.
  Path Parameter: id

🚀 Getting Started
  Prerequisites
  - Java 17+  
  - Maven 3+  
  - PostgreSQL  
  
 
