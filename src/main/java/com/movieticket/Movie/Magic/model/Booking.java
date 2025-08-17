package com.movieticket.Movie.Magic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;


    // No-argument constructor
    public Booking() {
    }

    // All-argument constructor
    public Booking(User user, Showtime showtime, LocalDateTime bookingTime, Double totalAmount, Integer numberOfSeats) {
        this.user = user;
        this.showtime = showtime;
        this.bookingTime = bookingTime;
        this.totalAmount = totalAmount;
        this.numberOfSeats = numberOfSeats;
    }

    // All-argument constructor
    public Booking(Long id, User user, Showtime showtime, LocalDateTime bookingTime, Double totalAmount, Integer numberOfSeats) {
        this.id = id;
        this.user = user;
        this.showtime = showtime;
        this.bookingTime = bookingTime;
        this.totalAmount = totalAmount;
        this.numberOfSeats = numberOfSeats;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }


}