package com.movieticket.Movie.Magic.repository;

import com.movieticket.Movie.Magic.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {


    List<Seat> findByTheaterId(Long theaterId);


    Optional<Seat> findByTheaterIdAndSeatRowAndSeatNumber(Long theaterId, String seatRow, Integer seatNumber);
}
