package com.movieticket.Movie.Magic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"seat_row", "seat_number", "theater_id"},
                name = "uk_seat_theater_row_number"))
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Many seats belong to one theater
    @JoinColumn(name = "theater_id", nullable = false)
    @NotNull(message = "Theater is required for a seat")
    private Theater theater;

    @NotBlank(message = "Seat row cannot be empty")
    @Size(max = 10, message = "Seat row cannot exceed 10 characters")
    @Column(name = "seat_row", nullable = false)
    private String seatRow;

    @NotNull(message = "Seat number is required")
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    private Boolean isPremium;

    // Constructors
    public Seat() {
    }

    public Seat(Theater theater, String seatRow, Integer seatNumber, Boolean isPremium) {
        this.theater = theater;
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

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
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