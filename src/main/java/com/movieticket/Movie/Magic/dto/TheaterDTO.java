package com.movieticket.Movie.Magic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TheaterDTO {

    private Long id;

    @NotBlank(message = "Theater name cannot be empty")
    @Size(max = 255, message = "Theater name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Theater location cannot be empty")
    @Size(max = 500, message = "Theater location cannot exceed 500 characters")
    private String location;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    // --- Constructors ---
    public TheaterDTO() {
    }

    // Constructor
    public TheaterDTO(Long id, String name, String location, Integer totalSeats) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalSeats = totalSeats;
    }

    // Constructor
    public TheaterDTO(String name, String location, Integer totalSeats) {
        this.name = name;
        this.location = location;
        this.totalSeats = totalSeats;
    }

    //  Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
}