package com.movieticket.Movie.Magic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SeatDTO {

    private Long id;

    @NotNull(message = "Theater ID is required for seat")
    private Long theaterId;

    private String theaterName;

    @NotBlank(message = "Seat row cannot be empty")
    @Size(max = 10, message = "Seat row cannot exceed 10 characters")
    private String seatRow;

    @NotNull(message = "Seat number is required")
    private Integer seatNumber;

    @NotNull(message = "Premium status is required")
    private Boolean isPremium;

    // Constructors
    public SeatDTO() {
    }

    // Full constructor
    public SeatDTO(Long id, Long theaterId, String theaterName, String seatRow, Integer seatNumber, Boolean isPremium) {
        this.id = id;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.isPremium = isPremium;
    }

    // Constructor for creating/updating
    public SeatDTO(Long theaterId, String seatRow, Integer seatNumber, Boolean isPremium) {
        this.theaterId = theaterId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.isPremium = isPremium;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
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
}
