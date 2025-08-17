package com.movieticket.Movie.Magic.controller;

import com.movieticket.Movie.Magic.dto.SeatDTO;
import com.movieticket.Movie.Magic.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    // Create Seat
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatDTO> createSeat(@Valid @RequestBody SeatDTO seatDTO) {
        SeatDTO createdSeat = seatService.createSeat(seatDTO);
        return new ResponseEntity<>(createdSeat, HttpStatus.CREATED);
    }

    //getSeatBy Id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable Long id) {
        SeatDTO seatDTO = seatService.getSeatById(id);
        return new ResponseEntity<>(seatDTO, HttpStatus.OK);
    }


    //getSeatsByTheater
    @GetMapping("/by-theater/{theaterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeatDTO>> getSeatsByTheater(@PathVariable Long theaterId) {
        List<SeatDTO> seats = seatService.getSeatsByTheaterId(theaterId);
        return new ResponseEntity<>(seats, HttpStatus.OK);
    }

   //updateSeat
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatDTO> updateSeat(@PathVariable Long id, @Valid @RequestBody SeatDTO seatDTO) {
        SeatDTO updatedSeat = seatService.updateSeat(id, seatDTO);
        return new ResponseEntity<>(updatedSeat, HttpStatus.OK);
    }

    //deleteSeat
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


   //status
    @GetMapping("/status/{showtimeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getSeatStatusForShowtime(@PathVariable Long showtimeId) {
        List<Map<String, Object>> seatStatuses = seatService.getSeatStatusForShowtime(showtimeId);
        return new ResponseEntity<>(seatStatuses, HttpStatus.OK);
    }
}