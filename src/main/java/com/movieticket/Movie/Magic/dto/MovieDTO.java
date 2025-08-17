package com.movieticket.Movie.Magic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class MovieDTO {


    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Genre cannot be empty")
    private String genre;

    @NotNull(message = "Duration in minutes is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 255, message = "Director name cannot exceed 255 characters")
    private String director;

    @Size(max = 1000, message = "Cast details cannot exceed 1000 characters")
    private String movieCast;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    // --- Constructors ---
    public MovieDTO() {
    }


    public MovieDTO(Long id, String title, String genre, Integer durationMinutes,
                    String description, String director, String movieCast, LocalDate releaseDate, String imageUrl) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.description = description;
        this.director = director;
        this.movieCast = movieCast;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
    }


    public MovieDTO(String title, String genre, Integer durationMinutes,
                    String description, String director, String movieCast, LocalDate releaseDate, String imageUrl) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.description = description;
        this.director = director;
        this.movieCast = movieCast;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
    }


    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getMovieCast() {
        return movieCast;
    }

    public void setMovieCast(String movieCast) {
        this.movieCast = movieCast;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}