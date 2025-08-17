package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.dto.MovieDTO;
import com.movieticket.Movie.Magic.exception.ResourceNotFoundException;
import com.movieticket.Movie.Magic.model.Movie;
import com.movieticket.Movie.Magic.repository.MovieRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;


    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        if (movieDTO.getId() != null) {
            movie.setId(movieDTO.getId());
        }
        movie.setTitle(movieDTO.getTitle());
        movie.setGenre(movieDTO.getGenre());
        movie.setDurationMinutes(movieDTO.getDurationMinutes());
        movie.setDescription(movieDTO.getDescription());
        movie.setDirector(movieDTO.getDirector());
        movie.setMovieCast(movieDTO.getMovieCast());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setImageUrl(movieDTO.getImageUrl());
        return movie;
    }


    private MovieDTO convertToDto(Movie movie) {
        if (movie == null) {
            return null;
        }
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getDurationMinutes(),
                movie.getDescription(),
                movie.getDirector(),
                movie.getMovieCast(),
                movie.getReleaseDate(),
                movie.getImageUrl()
        );
    }

    // --- CRUD Operations ---

    // Create a new movie
    public MovieDTO createMovie(MovieDTO movieDTO) {
        if (movieRepository.findByTitle(movieDTO.getTitle()).isPresent()) {
            throw new RuntimeException("Movie with title '" + movieDTO.getTitle() + "' already exists.");
        }
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDto(savedMovie);
    }

    // Get a movie by ID

    // Get all movies
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Update an existing movie
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        return movieRepository.findById(id)
                .map(existingMovie -> {
                    // Check if the new title conflicts with another movie's title (excluding itself)
                    Optional<Movie> movieWithSameTitle = movieRepository.findByTitle(movieDTO.getTitle());
                    if (movieWithSameTitle.isPresent() && !movieWithSameTitle.get().getId().equals(id)) {
                        throw new RuntimeException("Another movie with title '" + movieDTO.getTitle() + "' already exists.");
                    }

                    // Update fields
                    existingMovie.setTitle(movieDTO.getTitle());
                    existingMovie.setGenre(movieDTO.getGenre());
                    existingMovie.setDurationMinutes(movieDTO.getDurationMinutes());
                    existingMovie.setDescription(movieDTO.getDescription());
                    existingMovie.setDirector(movieDTO.getDirector());
                    existingMovie.setMovieCast(movieDTO.getMovieCast());
                    existingMovie.setReleaseDate(movieDTO.getReleaseDate());
                    existingMovie.setImageUrl(movieDTO.getImageUrl());

                    Movie updatedMovie = movieRepository.save(existingMovie);
                    return convertToDto(updatedMovie);
                }).orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
    }

    // Delete a movie by ID
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found with ID: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MovieDTO> searchMovies(String title, String genre) {
        List<Movie> movies;

        if (title != null && !title.isEmpty() && genre != null && !genre.isEmpty()) {
            // Search by both title OR genre
            movies = movieRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(title, genre);
        } else if (title != null && !title.isEmpty()) {
            // Search by title only
            movies = movieRepository.findByTitleContainingIgnoreCase(title);
        } else if (genre != null && !genre.isEmpty()) {
            // Search by genre only
            movies = movieRepository.findByGenreContainingIgnoreCase(genre);
        } else {
            // No search parameters, return all movies
            movies = movieRepository.findAll();
        }

        return movies.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public MovieDTO getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));
        return convertToDto(movie);
    }
}