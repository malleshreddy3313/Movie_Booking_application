package com.movieticket.Movie.Magic.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

@Entity
@Table(name = "showtimes")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(name = "showtime_date", nullable = false)
    private LocalDate showtimeDate;

    @Column(name = "showtime_time", nullable = false)
    private LocalTime showtimeTime;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "ticket_price", nullable = false)
    private BigDecimal ticketPrice;


    public Showtime() {
    }


    public Showtime(Long id, Movie movie, Theater theater, LocalDate showtimeDate, LocalTime showtimeTime, Integer availableSeats, BigDecimal ticketPrice) { // <-- Use BigDecimal
        this.id = id;
        this.movie = movie;
        this.theater = theater;
        this.showtimeDate = showtimeDate;
        this.showtimeTime = showtimeTime;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    // Constructor for creating a new Showtime
    public Showtime(Movie movie, Theater theater, LocalDate showtimeDate, LocalTime showtimeTime, Integer availableSeats, BigDecimal ticketPrice) { // <-- Use BigDecimal
        this.movie = movie;
        this.theater = theater;
        this.showtimeDate = showtimeDate;
        this.showtimeTime = showtimeTime;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    //  GETTERS
    public Long getId() { return id; }
    public Movie getMovie() { return movie; }
    public Theater getTheater() { return theater; }
    public LocalDate getShowtimeDate() { return showtimeDate; }
    public LocalTime getShowtimeTime() { return showtimeTime; }
    public Integer getAvailableSeats() { return availableSeats; }
    public BigDecimal getTicketPrice() { return ticketPrice; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public void setTheater(Theater theater) { this.theater = theater; }
    public void setShowtimeDate(LocalDate showtimeDate) { this.showtimeDate = showtimeDate; }
    public void setShowtimeTime(LocalTime showtimeTime) { this.showtimeTime = showtimeTime; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }

}