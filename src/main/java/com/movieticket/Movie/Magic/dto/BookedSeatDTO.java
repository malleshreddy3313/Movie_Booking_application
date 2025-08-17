package com.movieticket.Movie.Magic.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookedSeatDTO {

    private Long id;

    @NotNull(message = "Seat ID is required")
    private Long seatId;
    private String seatRow;
    private Integer seatNumber;
    private Boolean isPremium;

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    private String status;
    private LocalDateTime bookedAt;


    public BookedSeatDTO() {
    }


    public BookedSeatDTO(Long id, Long seatId, String seatRow, Integer seatNumber, Boolean isPremium,
                         Long showtimeId, String status, LocalDateTime bookedAt) {
        this.id = id;
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.isPremium = isPremium;
        this.showtimeId = showtimeId;
        this.status = status;
        this.bookedAt = bookedAt;
    }


    public BookedSeatDTO(Long seatId, Long showtimeId) {
        this.seatId = seatId;
        this.showtimeId = showtimeId;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}