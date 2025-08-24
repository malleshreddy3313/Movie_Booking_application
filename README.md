Movie Magic Backend

A robust and scalable backend for the Movie Magic movie ticketing application, built with Spring Boot, serving a modern, futuristic frontend. This backend provides all the necessary APIs for user authentication, movie management, booking, and administrative tasks.

🚀 Features
User Authentication: Secure registration and login functionality using JSON Web Tokens (JWT).

Movie & Showtime Management: CRUD (Create, Read, Update, Delete) operations for movies, cities, and showtimes.

Booking System: A core booking engine that handles seat selection and manages ticket reservations.

Email Notifications: Integration for sending email confirmations to users upon successful ticket booking.

Admin Dashboard APIs: Secure endpoints for administrators to manage content and view system statistics.

Database Integration: Persistent data storage for users, roles, movies, bookings, and more.

🛠️ Technology Stack
Framework: Spring Boot

Security: Spring Security & JWT

Database: (Specify your database, e.g., MySQL, PostgreSQL)

ORM: Hibernate / Spring Data JPA

Email Service: (Specify your email service, e.g., AWS SES, JavaMailSender)

Build Tool: Maven

📂 Project Structure
The project follows a standard Spring Boot and MVC-based architecture as per the above folder structure.
        

Authentication
POST /api/auth/register

POST /api/auth/login

GET /api/auth/me

Movie & Showtime Discovery
GET /api/movies?city=<city_name>

GET /api/showtimes?movieId=<movie_id>&cityId=<city_id>

GET /api/showtimes/{showtimeId}/seats

Booking & User Profile
POST /api/bookings

GET /api/users/me/bookings

Admin Management
POST /api/admin/movies

PUT /api/admin/movies/{movieId}

DELETE /api/admin/movies/{movieId}

GET /api/admin/stats

▶️ Getting Started
Clone the repository:
git clone https://github.com/your-username/movie-magic-backend.git

Navigate to the project directory:
cd movie-magic-backend

Configure your database:

Update the database connection details in src/main/resources/application.properties.

Ensure your database contains the necessary roles (e.g., ADMIN, USER). You may need to run a seeding script or manually insert them.

Build the project:
mvn clean install

Run the application:
mvn spring-boot:run

The application will start on http://localhost:8080 (or your configured port).
  
 
