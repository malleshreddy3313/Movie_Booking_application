package com.movieticket.Movie.Magic.controller;

import com.movieticket.Movie.Magic.dto.ShowtimeDTO;
import com.movieticket.Movie.Magic.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

   //create show time
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDTO> createShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO createdShowtime = showtimeService.createShowtime(showtimeDTO);
        return new ResponseEntity<>(createdShowtime, HttpStatus.CREATED);
    }

   //retrieve show time by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ShowtimeDTO> getShowtimeById(@PathVariable Long id) {
        ShowtimeDTO showtimeDTO = showtimeService.getShowtimeById(id);
        return ResponseEntity.ok(showtimeDTO);
    }

   //get show times
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ShowtimeDTO>> getAllShowtimes() {
        List<ShowtimeDTO> showtimes = showtimeService.getAllShowtimes();
        return ResponseEntity.ok(showtimes);
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ShowtimeDTO>> searchShowtimes(
            @RequestParam(required = false) String movieTitle,
            @RequestParam(required = false) String theaterName,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShowtimeDTO> showtimes = showtimeService.searchShowtimes(movieTitle, theaterName, date);
        return ResponseEntity.ok(showtimes);
    }

   //update show time
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDTO> updateShowtime(@PathVariable Long id, @Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO updatedShowtime = showtimeService.updateShowtime(id, showtimeDTO);
        return ResponseEntity.ok(updatedShowtime);
    }

  //delete a show time
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.ok("Showtime with ID: " + id + " deleted successfully.");
    }
}