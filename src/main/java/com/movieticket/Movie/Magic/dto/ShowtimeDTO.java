package com.movieticket.Movie.Magic.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;



public class ShowtimeDTO {
    private Long id;
    private Long movieId;
    private Long theaterId;
    private LocalDate showtimeDate;
    private LocalTime showtimeTime;
    private Integer availableSeats;
    private BigDecimal ticketPrice;



    public ShowtimeDTO() {
    }

    // All-argument constructor
    public ShowtimeDTO(Long id, Long movieId, Long theaterId, LocalDate showtimeDate, LocalTime showtimeTime, Integer availableSeats, BigDecimal ticketPrice) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.showtimeDate = showtimeDate;
        this.showtimeTime = showtimeTime;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }


    public ShowtimeDTO(Long movieId, Long theaterId, LocalDate showtimeDate, LocalTime showtimeTime, Integer availableSeats,BigDecimal ticketPrice) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.showtimeDate = showtimeDate;
        this.showtimeTime = showtimeTime;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }


    public Long getId() {
        return id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public LocalDate getShowtimeDate() {
        return showtimeDate;
    }

    public LocalTime getShowtimeTime() {
        return showtimeTime;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
    public Integer getAvailableSeats() {
        return availableSeats;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public void setShowtimeDate(LocalDate showtimeDate) {
        this.showtimeDate = showtimeDate;
    }

    public void setShowtimeTime(LocalTime showtimeTime) {
        this.showtimeTime = showtimeTime;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}