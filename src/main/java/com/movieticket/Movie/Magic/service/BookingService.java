package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.dto.BookingDTO;
import com.movieticket.Movie.Magic.exception.BadRequestException;
import com.movieticket.Movie.Magic.exception.ResourceNotFoundException;
import com.movieticket.Movie.Magic.model.Booking;
import com.movieticket.Movie.Magic.model.Showtime;
import com.movieticket.Movie.Magic.model.User;
import com.movieticket.Movie.Magic.repository.BookingRepository;
import com.movieticket.Movie.Magic.repository.ShowtimeRepository;
import com.movieticket.Movie.Magic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private EmailService emailService;




    private BookingDTO convertToDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser() != null ? booking.getUser().getId() : null);
        dto.setShowtimeId(booking.getShowtime() != null ? booking.getShowtime().getId() : null);
        dto.setBookingTime(booking.getBookingTime());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setNumberOfSeats(booking.getNumberOfSeats());

        return dto;
    }

    // Helper method to convert BookingDTO to Booking Entity

    private Booking convertToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        if (bookingDTO.getId() != null) {
            booking.setId(bookingDTO.getId());
        }
        booking.setBookingTime(bookingDTO.getBookingTime());
        booking.setTotalAmount(bookingDTO.getTotalAmount());
        booking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
        return booking;
    }



    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        // 1. Fetch User and Showtime entities
        User user = userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + bookingDTO.getUserId()));

        Showtime showtime = showtimeRepository.findById(bookingDTO.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with ID: " + bookingDTO.getShowtimeId()));

        // 2. Validate seat availability
        if (showtime.getAvailableSeats() < bookingDTO.getNumberOfSeats()) {
            throw new BadRequestException("Not enough seats available for this showtime. Available: " +
                    showtime.getAvailableSeats() + ", Requested: " + bookingDTO.getNumberOfSeats());
        }

        // 3. Update available seats for the showtime
        showtime.setAvailableSeats(showtime.getAvailableSeats() - bookingDTO.getNumberOfSeats());
        showtimeRepository.save(showtime); // Save the updated showtime

        // 4. Create and save the booking entity
        Booking booking = new Booking(); // Create a new Booking instance
        booking.setUser(user);
        booking.setShowtime(showtime);
        booking.setBookingTime(LocalDateTime.now()); // Set current booking time
        booking.setTotalAmount(bookingDTO.getTotalAmount());
        booking.setNumberOfSeats(bookingDTO.getNumberOfSeats());


        Booking savedBooking = bookingRepository.save(booking);

        // 5. Send Booking Confirmation Email
        try {
            String toEmail = user.getEmail();
            String subject = "Movie Magic: Your Booking Confirmation!";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "Your booking for the movie '%s' at '%s' on %s at %s has been confirmed.\n" +
                            "Booking ID: %d\n" +
                            "Number of Seats: %d\n" +
                            "Total Amount: %.2f\n\n" +
                            "Thank you for choosing Movie Magic!\n",
                    user.getUsername(),
                    showtime.getMovie().getTitle(),
                    showtime.getTheater().getName(),
                    showtime.getShowtimeDate().toString(),
                    showtime.getShowtimeTime().toString(),
                    savedBooking.getId(),
                    savedBooking.getNumberOfSeats(),
                    savedBooking.getTotalAmount()
            );
            emailService.sendSimpleEmail(toEmail, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send booking confirmation email for booking ID " + savedBooking.getId() + ": " + e.getMessage());

        }

        return convertToDto(savedBooking);
    }


    @Transactional(readOnly = true)
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return convertToDto(booking);
    }


    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));



        if (bookingDTO.getUserId() != null && !existingBooking.getUser().getId().equals(bookingDTO.getUserId())) {
            User newUser = userRepository.findById(bookingDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("New User not found with ID: " + bookingDTO.getUserId()));
            existingBooking.setUser(newUser);
        }

        if (bookingDTO.getShowtimeId() != null && !existingBooking.getShowtime().getId().equals(bookingDTO.getShowtimeId())) {
            Showtime newShowtime = showtimeRepository.findById(bookingDTO.getShowtimeId())
                    .orElseThrow(() -> new ResourceNotFoundException("New Showtime not found with ID: " + bookingDTO.getShowtimeId()));
            existingBooking.setShowtime(newShowtime);
        }

        if (bookingDTO.getTotalAmount() != null) {
            existingBooking.setTotalAmount(bookingDTO.getTotalAmount());
        }
        if (bookingDTO.getNumberOfSeats() != null) {

            existingBooking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
        }


        Booking updatedBooking = bookingRepository.save(existingBooking);
        return convertToDto(updatedBooking);
    }


    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        // Revert seats for the showtime (important!)
        Showtime showtime = booking.getShowtime();
        if (showtime != null) {
            showtime.setAvailableSeats(showtime.getAvailableSeats() + booking.getNumberOfSeats());
            showtimeRepository.save(showtime);
        }

        // Optional: Send a cancellation email
        try {
            String toEmail = booking.getUser().getEmail();
            String subject = "Movie Magic: Your Booking Has Been Cancelled";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "Your booking (ID: %d) for '%s' on %s at %s has been cancelled.\n" +
                            "If you had paid, a refund will be processed shortly.\n\n" +
                            "We hope to see you soon!\n",
                    booking.getUser().getUsername(),
                    booking.getId(),
                    booking.getShowtime().getMovie().getTitle(),
                    booking.getShowtime().getShowtimeDate().toString(),
                    booking.getShowtime().getShowtimeTime().toString()
            );
            emailService.sendSimpleEmail(toEmail, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send cancellation email for booking ID " + booking.getId() + ": " + e.getMessage());
        }

        bookingRepository.delete(booking);
    }
}