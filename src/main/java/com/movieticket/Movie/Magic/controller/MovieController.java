package com.movieticket.Movie.Magic.controller;

import com.movieticket.Movie.Magic.dto.MovieDTO;
import com.movieticket.Movie.Magic.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    //  Create Movie
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        try {
            MovieDTO createdMovie = movieService.createMovie(movieDTO);
            return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //  Get All Movies
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre) {
        List<MovieDTO> movies = movieService.searchMovies(title, genre);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }


    //  Update Movie
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO) {
        try {
            MovieDTO updatedMovie = movieService.updateMovie(id, movieDTO);
            return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Delete Movie
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        MovieDTO movieDTO = movieService.getMovieById(id);
        return new ResponseEntity<>(movieDTO, HttpStatus.OK);
    }
}