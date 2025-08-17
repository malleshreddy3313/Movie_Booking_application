package com.movieticket.Movie.Magic.controller;

import com.movieticket.Movie.Magic.dto.TheaterDTO;
import com.movieticket.Movie.Magic.service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    // Create Theater
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterDTO> createTheater(@Valid @RequestBody TheaterDTO theaterDTO) {
        try {
            TheaterDTO createdTheater = theaterService.createTheater(theaterDTO);
            return new ResponseEntity<>(createdTheater, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //  Get All Theaters
    @GetMapping

    public ResponseEntity<List<TheaterDTO>> getAllTheaters() {
        List<TheaterDTO> theaters = theaterService.getAllTheaters();
        return new ResponseEntity<>(theaters, HttpStatus.OK); // 200 OK
    }

    //  Get Theater by ID
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDTO> getTheaterById(@PathVariable Long id) {
        return theaterService.getTheaterById(id)
                .map(theaterDTO -> new ResponseEntity<>(theaterDTO, HttpStatus.OK)) // 200 OK if found
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found if not
    }

    // Update Theater
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterDTO> updateTheater(@PathVariable Long id, @Valid @RequestBody TheaterDTO theaterDTO) {
        try {
            TheaterDTO updatedTheater = theaterService.updateTheater(id, theaterDTO);
            return new ResponseEntity<>(updatedTheater, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Delete Theater
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        try {
            theaterService.deleteTheater(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
